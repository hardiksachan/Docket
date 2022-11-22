package com.hardiksachan.auth_domain

import arrow.core.Either
import arrow.core.Option
import kotlinx.coroutines.flow.Flow

interface AuthFacade {
    val user: Flow<Option<User>>

    suspend fun registerWithEmailAndPassword(
        emailAddress: EmailAddress,
        password: Password,
    ): Either<AuthFailure, Unit>

    suspend fun signInWithEmailAndPassword(
        emailAddress: EmailAddress,
        password: Password,
    ): Either<AuthFailure, Unit>

    suspend fun signInWithToken(
        token: Token
    ): Either<AuthFailure, Unit>

    suspend fun signOut()
}