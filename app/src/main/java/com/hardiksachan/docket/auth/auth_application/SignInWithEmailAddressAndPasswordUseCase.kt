package com.hardiksachan.docket.auth.auth_application

import arrow.core.Either
import com.hardiksachan.docket.auth.auth_domain.AuthFacade
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password

class SignInWithEmailAddressAndPasswordUseCase(
    private val authFacade: AuthFacade
)  {
    suspend fun execute(
        emailAddress: EmailAddress,
        password: Password
    ): Either<AuthFailure, Unit> = authFacade.signInWithEmailAndPassword(emailAddress, password)
}