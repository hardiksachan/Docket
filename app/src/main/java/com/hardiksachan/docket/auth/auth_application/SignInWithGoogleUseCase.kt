package com.hardiksachan.docket.auth.auth_application

import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.left
import com.hardiksachan.docket.auth.auth_domain.AuthFacade
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_framework.GoogleTokenGenerator

interface SignInWithGoogleUseCase {
    suspend fun execute(): Either<AuthFailure, Unit>
}

class SignInWithGoogleUseCaseImpl(
    private val authFacade: AuthFacade,
    private val tokenGenerator: GoogleTokenGenerator
) : SignInWithGoogleUseCase {
    override suspend fun execute(): Either<AuthFailure, Unit> = authFacade
        .signInWithToken(
            tokenGenerator
                .generate()
                .getOrHandle { return AuthFailure.TokenNotGenerated(it).left() }
        )
}