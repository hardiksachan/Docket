package com.hardiksachan.auth_application.usecases

import arrow.core.Either
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import javax.inject.Inject

class RegisterWithEmailAddressAndPasswordUseCase @Inject constructor(
    private val authFacade: AuthFacade
) {
    suspend fun execute(
        emailAddress: EmailAddress,
        password: Password
    ): Either<AuthFailure, Unit>  = authFacade.registerWithEmailAndPassword(emailAddress, password)
}