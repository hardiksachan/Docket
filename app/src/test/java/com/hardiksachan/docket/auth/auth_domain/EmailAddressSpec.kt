package com.hardiksachan.docket.auth.auth_domain

import com.hardiksachan.docket.core.failures.ValueFailure
import io.kotest.core.spec.style.BehaviorSpec
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight

class EmailAddressSpec : BehaviorSpec({
    Given("an email address string") {
        When("is a valid email") {
            val validEmailStr = "abc@xyz.com"
            Then("an EmailAddress with valid email is created") {
                val email = EmailAddress.create(validEmailStr)

                expectThat(email.value)
                    .isRight(validEmailStr)
            }
        }

        When("is NOT a valid email") {
            val invalidEmailStr = "dgnaudbg"
            Then("an EmailAddress with ValueFailure is created") {
                val email = EmailAddress.create(invalidEmailStr)

                expectThat(email.value)
                    .isLeft(ValueFailure.InvalidEmail(invalidEmailStr))
            }
        }
    }
})
