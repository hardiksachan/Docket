package com.hardiksachan.auth_application

import arrow.core.Either
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class RegisterWithEmailAddressAndPasswordUseCaseSpec : FunSpec({
    val mockAuthFacade: AuthFacade = mockk()

    lateinit var sut: RegisterWithEmailAddressAndPasswordUseCase

    beforeTest {
        sut = RegisterWithEmailAddressAndPasswordUseCase(mockAuthFacade)
    }

    context("execute called") {
        test("call is forwarded to auth facade") {
            // arrange
            val emailSlot = slot<EmailAddress>()
            val passwordSlot = slot<Password>()

            val forwardedCallResult: Either<AuthFailure, Unit> = mockk()

            coEvery {
                mockAuthFacade
                    .registerWithEmailAndPassword(capture(emailSlot), capture(passwordSlot))
            } returns forwardedCallResult

            // act
            val result = sut.execute(
                EmailAddress.create("user@app.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result) isEqualTo forwardedCallResult

            expect {
                that(emailSlot.captured) isEqualTo EmailAddress.create("user@app.com")
                that(passwordSlot.captured) isEqualTo Password.create("12345678")
            }
        }
    }
})
