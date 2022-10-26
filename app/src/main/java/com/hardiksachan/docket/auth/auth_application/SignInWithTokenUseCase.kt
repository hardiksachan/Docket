package com.hardiksachan.docket.auth.auth_application

import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.left
import com.hardiksachan.docket.auth.auth_domain.AuthFacade
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.TokenGeneratorFacade

class SignInWithTokenUseCase(
    private val authFacade: AuthFacade,
    private val tokenGenerator: TokenGeneratorFacade
) {
    suspend fun execute(): Either<AuthFailure, Unit> = tokenGenerator
        .generate()
        .getOrHandle { return AuthFailure.TokenNotGenerated(it).left() }
        .let { token ->
            authFacade.signInWithToken(token)
        }
}