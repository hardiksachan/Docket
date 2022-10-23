package com.hardiksachan.docket.auth.auth_domain

import com.hardiksachan.docket.core.failures.ValueFailure
import io.kotest.core.spec.style.BehaviorSpec
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight

class PasswordSpec : BehaviorSpec({
    Given("a password string") {
        When("is a valid password") {
            val validPasswordStr = "ajvfakfga"
            Then("a Password with valid password is created") {
                val password = Password.create(validPasswordStr)

                expectThat(password.value)
                    .isRight(validPasswordStr)
            }
        }

        When("is NOT a valid password") {
            And("is a short password") {
                val shortPasswordStr = "asvb"
                Then("a Password with ShortPassword Failure is created") {
                    val password = Password.create(shortPasswordStr)

                    expectThat(password.value)
                        .isLeft(ValueFailure.ShortPassword(shortPasswordStr))
                }
            }
        }
    }
})

