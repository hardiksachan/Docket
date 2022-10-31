package com.hardiksachan.auth_domain

import arrow.core.Either

interface TokenGeneratorFacade {
    suspend fun generate(): Either<Token.GenerationFailure, Token>
}