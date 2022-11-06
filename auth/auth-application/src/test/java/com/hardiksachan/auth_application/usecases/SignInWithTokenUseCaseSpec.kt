package com.hardiksachan.auth_application.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.Token
import com.hardiksachan.auth_domain.TokenFacade
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.assertions.isEqualTo

class SignInWithTokenUseCaseSpec : FunSpec({
    val mockAuthFacade: AuthFacade = mockk()
    val mockTokenGenerator: TokenFacade = mockk()

    lateinit var sut: SignInWithTokenUseCase

    beforeTest {
        sut = SignInWithTokenUseCase(mockAuthFacade, mockTokenGenerator)
    }

    afterTest {
        clearAllMocks()
    }

    context("execute called") {
        test("token is generated successfully and call is forwarded to auth facade") {
            // arrange
            val tokenSlot = slot<Token>()

            val mockToken = mockk<Token>()
            val forwardedCallResult: Either<AuthFailure, Unit> = mockk()

            coEvery {
                mockTokenGenerator.generate()
            } returns mockToken.right()

            coEvery {
                mockAuthFacade.signInWithToken(capture(tokenSlot))
            } returns forwardedCallResult

            // act
            val result = sut.execute()

            // assert
            expectThat(result) isEqualTo forwardedCallResult

            expectThat(tokenSlot.captured) isEqualTo mockToken
        }
        test("token generation fails and NO call is forwarded to auth facade") {
            // arrange
            val mockFailure = mockk<Token.GenerationFailure>()

            coEvery {
                mockTokenGenerator.generate()
            } returns mockFailure.left()

            // act
            val result = sut.execute()

            // assert
            expectThat(result).isLeft(AuthFailure.TokenNotGenerated(mockFailure))

            verify {
                mockAuthFacade wasNot Called
            }
        }
    }
})
