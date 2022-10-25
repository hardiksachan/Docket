package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either

interface AuthFacade {
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
}