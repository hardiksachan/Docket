package com.hardiksachan.auth_application.usecases

import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.left
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.TokenFacade
import javax.inject.Inject

class SignInWithTokenUseCase @Inject constructor(
    private val authFacade: AuthFacade,
    private val tokenGenerator: TokenFacade,
) {
    suspend fun execute(): Either<AuthFailure, Unit> = tokenGenerator
        .generate()
        .getOrHandle { return AuthFailure.TokenNotGenerated(it).left() }
        .let { token ->
            authFacade.signInWithToken(token)
        }
}