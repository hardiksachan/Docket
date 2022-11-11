package com.hardiksachan.auth_framework

import arrow.core.some
import com.hardiksachan.MainCoroutineListener
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import com.hardiksachan.auth_domain.Token
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.mockk.*
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.auth.EmailPasswordAuth
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight

class RealmAuthFacadeSpec : FunSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val realmAppMock = mockk<App>()
    val emailPasswordAuthMock = mockk<EmailPasswordAuth>()

    lateinit var realmAuthFacade: RealmAuthFacade

    beforeTest {
        realmAuthFacade = RealmAuthFacade(realmAppMock)

        every {
            realmAppMock.emailPasswordAuth
        } returns emailPasswordAuthMock
    }

    afterTest {
        clearAllMocks()
    }

    context("registerWithEmailAndPassword") {
        test("registration is successful, expect no error response") {
            // arrange
            coEvery {
                emailPasswordAuthMock.registerUser(any(), any())
            } returns Unit

            // act
            val result = realmAuthFacade.registerWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isRight(Unit)

            coVerify(exactly = 1) {
                emailPasswordAuthMock
                    .registerUser(
                        eq("john@gmail.com"),
                        eq("12345678"),
                    )
            }
        }

        withData<Pair<() -> Exception, AuthFailure>>(
            nameFn = { "registration is unsuccessful with ${it.first}, expect error response with ${it.second} failure" },
            { mockk<UserAlreadyExistsException>() } to AuthFailure.EmailAlreadyInUse,
            { mockk<ServiceException>() } to AuthFailure.ServerError
        ) { (exceptionGen, failure) ->
            // arrange
            coEvery {
                emailPasswordAuthMock.registerUser(any(), any())
            } throws exceptionGen()

            // act
            val result = realmAuthFacade.registerWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isLeft(failure)

            coVerify(exactly = 1) {
                emailPasswordAuthMock
                    .registerUser(
                        eq("john@gmail.com"),
                        eq("12345678"),
                    )
            }
        }
    }

    context("signInWithEmailAndPassword") {

        mockkObject(Credentials) {

            every {
                Credentials.emailPassword(any(), any())
            } returns mockk()

            test("login is successful, expect no error response") {
                // arrange
                coEvery {
                    realmAppMock.login(any())
                } returns mockk()

                // act
                val result = realmAuthFacade.signInWithEmailAndPassword(
                    EmailAddress.create("john@gmail.com"),
                    Password.create("12345678")
                )

                // assert
                expectThat(result).isRight(Unit)

                verify(exactly = 1) {
                    Credentials.emailPassword(
                        eq("john@gmail.com"),
                        eq("12345678"),
                    )
                }
            }

            withData<Pair<() -> Exception, AuthFailure>>(
                nameFn = { "login failed with ${it.first}, expect error response with ${it.second} failure" },
                { mockk<InvalidCredentialsException>() } to AuthFailure.InvalidEmailAndPasswordCombination,
                { mockk<ServiceException>() } to AuthFailure.ServerError
            ) { (exceptionGen, failure) ->
                // arrange
                coEvery {
                    realmAppMock.login(any())
                } throws exceptionGen()

                // act
                val result = realmAuthFacade.signInWithEmailAndPassword(
                    EmailAddress.create("john@gmail.com"),
                    Password.create("12345678")
                )

                // assert
                expectThat(result).isLeft(failure)

                verify(exactly = 1) {
                    Credentials
                        .emailPassword(
                            eq("john@gmail.com"),
                            eq("12345678"),
                        )
                }
            }
        }
    }

    context("signInWithToken") {
        context("google") {

            mockkObject(Credentials) {

                every {
                    Credentials.jwt(any())
                } returns mockk()

                test("login is successful, expect no error response") {
                    // arrange
                    coEvery {
                        realmAppMock.login(any())
                    } returns mockk()

                    // act
                    val result = realmAuthFacade.signInWithToken(
                        Token.Google(
                            "googleIdToken",
                            "googleAccessToken".some()
                        )
                    )

                    // assert
                    expectThat(result).isRight(Unit)

                    verify(exactly = 1) {
                        Credentials.jwt(
                            eq("googleIdToken"),
                        )
                    }
                }

                withData<Pair<() -> Exception, AuthFailure>>(
                    nameFn = { "login failed with ${it.first}, expect error response with ${it.second} failure" },
                    { mockk<InvalidCredentialsException>() } to AuthFailure.InvalidToken,
                    { mockk<ServiceException>() } to AuthFailure.ServerError
                ) { (exceptionGen, failure) ->
                    // arrange
                    coEvery {
                        realmAppMock.login(any())
                    } throws exceptionGen()

                    // act
                    val result = realmAuthFacade.signInWithToken(
                        Token.Google(
                            "googleIdToken",
                            "googleAccessToken".some()
                        )
                    )

                    // assert
                    expectThat(result).isLeft(failure)

                    verify(exactly = 1) {
                        Credentials.jwt(
                            eq("googleIdToken"),
                        )
                    }
                }
            }
        }
    }
})
