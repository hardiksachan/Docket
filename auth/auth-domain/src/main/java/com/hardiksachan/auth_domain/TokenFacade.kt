package com.hardiksachan.auth_domain

import arrow.core.Either

interface TokenFacade {
    suspend fun generate(): Either<Token.Failure, Token>
    suspend fun invalidate(): Either<Token.Failure, Unit>
}