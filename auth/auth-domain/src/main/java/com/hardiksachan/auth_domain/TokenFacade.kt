package com.hardiksachan.auth_domain

import arrow.core.Either

interface TokenFacade {
    suspend fun generate(): Either<Token.GenerationFailure, Token>
}