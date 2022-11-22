package com.hardiksachan.auth_framework

import arrow.core.*
import com.hardiksachan.auth_domain.*
import com.hardiksachan.core.UniqueID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import io.realm.kotlin.mongodb.User as rUser

@Singleton
class RealmAuthFacade @Inject constructor(
    private val realmApp: App,
) : AuthFacade {

    private val realmUserCh = Channel<Option<rUser>>()
    private val realmUser: Flow<Option<rUser>> = flow {
        emit(None)
        emit(realmUserCh.receive())
    }

    override val user: Flow<Option<User>>
        get() = realmUser.map { userOption -> userOption.map { it.toDomain() } }

    override suspend fun registerWithEmailAndPassword(
        emailAddress: EmailAddress, password: Password
    ): Either<AuthFailure, Unit> {
        val emailAddressStr = emailAddress.getOrCrash()
        val passwordStr = password.getOrCrash()

        return try {
            realmApp.emailPasswordAuth.registerUser(emailAddressStr, passwordStr)
            Unit.right()
        } catch (e: UserAlreadyExistsException) {
            AuthFailure.EmailAlreadyInUse.left()
        } catch (e: ServiceException) {
            AuthFailure.ServerError.left()
        }
    }

    override suspend fun signInWithEmailAndPassword(
        emailAddress: EmailAddress, password: Password
    ): Either<AuthFailure, Unit> =
        try {
            val loggedInUser = realmApp.login(
                Credentials.emailPassword(
                    emailAddress.getOrCrash(), password.getOrCrash()
                )
            )
            realmUserCh.send(loggedInUser.some())
            Unit.right()
        } catch (e: InvalidCredentialsException) {
            AuthFailure.InvalidEmailAndPasswordCombination.left()
        } catch (e: ServiceException) {
            AuthFailure.ServerError.left()
        }

    override suspend fun signInWithToken(token: Token): Either<AuthFailure, Unit> =
        loginWithCredential(token.toCredential())

    override suspend fun signOut() {
        when (val currentUser = realmUser.last()){
            // TODO: improve error reporting
            None -> throw IllegalStateException("Log out called with no signed in user")
            is Some -> {
                currentUser.value.logOut()
                realmUserCh.send(None)
            }
        }
    }

    private fun Token.toCredential(): Credentials = when (this) {
        is Token.Google -> {
            Credentials.jwt(idToken)
        }
    }

    private suspend fun loginWithCredential(credentials: Credentials): Either<AuthFailure, Unit> =
        try {
            realmApp.login(credentials)
            Unit.right()
        } catch (e: InvalidCredentialsException) {
            AuthFailure.InvalidToken.left()
        } catch (e: ServiceException) {
            AuthFailure.ServerError.left()
        }
}

private fun rUser.toDomain(): User  = User(
    id = UniqueID.fromUniqueString(id),
    emailAddress = EmailAddress.create("") // TODO: get user email
)