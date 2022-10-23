package com.hardiksachan.docket.auth.auth_application

import arrow.core.Either
import com.hardiksachan.docket.auth.auth_domain.AuthFailure

interface SignInWithGoogleUseCase {
    suspend fun execute(): Either<AuthFailure, Unit>
}