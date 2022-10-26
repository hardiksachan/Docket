package com.hardiksachan.docket.auth.auth_application

import arrow.core.Either
import com.hardiksachan.docket.auth.auth_domain.AuthFacade
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password

interface RegisterWithEmailAddressAndPasswordUseCase {
    suspend fun execute(
        emailAddress: EmailAddress,
        password: Password
    ): Either<AuthFailure, Unit>
}

class RegisterWithEmailAddressAndPasswordUseCaseImpl(
    private val authFacade: AuthFacade
): RegisterWithEmailAddressAndPasswordUseCase {
    override suspend fun execute(
        emailAddress: EmailAddress,
        password: Password
    ): Either<AuthFailure, Unit>  = authFacade.registerWithEmailAndPassword(emailAddress, password)
}