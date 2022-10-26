package com.hardiksachan.docket.auth.auth_infrastructure

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.right
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.hardiksachan.docket.auth.auth_domain.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias GoogleAuthCredentialProvider = (idToken: String, accessToken: Option<String>) -> AuthCredential

class FirebaseAuthFacade(
    private val auth: FirebaseAuth,
    private val googleAuthCredentialProvider: GoogleAuthCredentialProvider
) : AuthFacade {
    override suspend fun registerWithEmailAndPassword(
        emailAddress: EmailAddress, password: Password
    ): Either<AuthFailure, Unit> {
        val emailAddressStr = emailAddress.getOrCrash()
        val passwordStr = password.getOrCrash()

        return auth.createUserWithEmailAndPassword(emailAddressStr, passwordStr).awaitCompletion()
            .fold({ exception ->
                return when (exception) {
                    is FirebaseAuthUserCollisionException -> AuthFailure.EmailAlreadyInUse
                    else -> AuthFailure.ServerError
                }.left()
            }, { Unit.right() })
    }

    override suspend fun signInWithEmailAndPassword(
        emailAddress: EmailAddress, password: Password
    ): Either<AuthFailure, Unit> {
        val emailAddressStr = emailAddress.getOrCrash()
        val passwordStr = password.getOrCrash()

        return auth.signInWithEmailAndPassword(emailAddressStr, passwordStr).awaitCompletion()
            .fold({ exception ->
                return when (exception) {
                    is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> AuthFailure.InvalidEmailAndPasswordCombination
                    else -> AuthFailure.ServerError
                }.left()
            }, { Unit.right() })
    }

    override suspend fun signInWithToken(token: Token): Either<AuthFailure, Unit> {
        val credential = token.mapToCredential()

        return auth.signInWithCredential(credential)
            .awaitCompletion()
            .fold(
                { exception ->
                    when (exception) {
                        is FirebaseAuthInvalidUserException,
                        is FirebaseAuthInvalidCredentialsException ->
                            AuthFailure.InvalidToken
                        is FirebaseAuthUserCollisionException ->
                            AuthFailure.EmailAlreadyInUse
                        else -> AuthFailure.ServerError
                    }.left()
                },
                { Unit.right() }
            )
    }

    private fun Token.mapToCredential(): AuthCredential = when (this) {
        is Token.Google -> googleAuthCredentialProvider(idToken, accessToken)
    }
}

suspend fun <T> Task<T>.awaitCompletion() = suspendCoroutine<Either<Exception?, T>> { cont ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            cont.resume(task.result.right())
        } else {
            cont.resume(task.exception.left())
        }
    }
}