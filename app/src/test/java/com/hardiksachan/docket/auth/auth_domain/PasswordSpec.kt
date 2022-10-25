package com.hardiksachan.docket.auth.auth_domain

import com.hardiksachan.docket.core.failures.ValueFailure
import io.kotest.core.spec.style.FunSpec
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight

class PasswordSpec : FunSpec({
    context("a possible password string") {
        test("is a valid password, a Password with valid password is created") {
            // arrange
            val validPasswordStr = "ajvfakfga"

            // act
            val password = Password.create(validPasswordStr)

            // assert
            expectThat(password.value)
                .isRight(validPasswordStr)
        }
        test("is a short password, a Password with ShortPassword Failure is created") {
            // arrange
            val shortPasswordStr = "asvb"

            // act
            val password = Password.create(shortPasswordStr)

            // assert
            expectThat(password.value)
                .isLeft(ValueFailure.ShortPassword(shortPasswordStr))
        }
    }
})

