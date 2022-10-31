package com.hardiksachan.auth_application

import arrow.core.Either
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password

class SignInWithEmailAddressAndPasswordUseCase(
    private val authFacade: AuthFacade
)  {
    suspend fun execute(
        emailAddress: EmailAddress,
        password: Password
    ): Either<AuthFailure, Unit> = authFacade.signInWithEmailAndPassword(emailAddress, password)
}