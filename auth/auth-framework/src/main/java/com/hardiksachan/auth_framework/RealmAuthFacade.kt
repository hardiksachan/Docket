package com.hardiksachan.auth_framework

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.hardiksachan.auth_domain.*
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmAuthFacade @Inject constructor(
    private val realmApp: App,
) : AuthFacade {
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
            realmApp.login(
                Credentials.emailPassword(
                    emailAddress.getOrCrash(), password.getOrCrash()
                )
            )
            Unit.right()
        } catch (e: InvalidCredentialsException) {
            AuthFailure.InvalidEmailAndPasswordCombination.left()
        } catch (e: ServiceException) {
            AuthFailure.ServerError.left()
        }

    override suspend fun signInWithToken(token: Token): Either<AuthFailure, Unit> =
        loginWithCredential(token.toCredential())

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