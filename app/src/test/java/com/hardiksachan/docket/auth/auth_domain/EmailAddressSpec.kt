package com.hardiksachan.docket.auth.auth_domain

import com.hardiksachan.docket.core.failures.ValueFailure
import io.kotest.core.spec.style.FunSpec
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight

class EmailAddressSpec : FunSpec({
    context("a possible email address string") {
        test("is a valid email, an EmailAddress with valid email is created") {
            // arrange
            val validEmailStr = "abc@xyz.com"

            // act
            val email = EmailAddress.create(validEmailStr)

            // assert
            expectThat(email.value)
                .isRight(validEmailStr)
        }
        test("is NOT a valid email, an EmailAddress with ValueFailure is created") {
            // arrange
            val invalidEmailStr = "dgnaudbg"

            // act
            val email = EmailAddress.create(invalidEmailStr)

            // assert
            expectThat(email.value)
                .isLeft(ValueFailure.InvalidEmail(invalidEmailStr))
        }
    }
})
