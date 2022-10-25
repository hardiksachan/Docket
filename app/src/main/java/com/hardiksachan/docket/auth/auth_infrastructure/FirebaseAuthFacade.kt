package com.hardiksachan.docket.auth.auth_infrastructure

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.hardiksachan.docket.auth.auth_domain.AuthFacade
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthFacade(
    private val auth: FirebaseAuth
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

    override suspend fun signInWithGoogle(): Either<AuthFailure, Unit> {
        return Unit.right()
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