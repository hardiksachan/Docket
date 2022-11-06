package com.hardiksachan.auth_application

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.hardiksachan.auth_application.usecases.RegisterWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.usecases.SignInWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.usecases.SignInWithTokenUseCase
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import strikt.api.expect
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class AuthPresenterSpec : FunSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val signInWithTokenUseCaseMock =
        mockk<SignInWithTokenUseCase>()
    val registerWithEmailAddressAndPasswordUseCaseMock =
        mockk<RegisterWithEmailAddressAndPasswordUseCase>()
    val signInWithEmailAddressAndPasswordUseCaseMock =
        mockk<SignInWithEmailAddressAndPasswordUseCase>()

    lateinit var vm: AuthPresenter

    beforeTest {
        vm = AuthPresenter(
            signInWithTokenUseCaseMock,
            registerWithEmailAddressAndPasswordUseCaseMock,
            signInWithEmailAddressAndPasswordUseCaseMock
        )
    }

    context("email address is changed") {
        test("email address is updated and auth failure option is reset") {
            // arrange
            val updatedEmailStr = "abc@xyz.com"

            // act
            vm.handleEvent(AuthPresenter.Event.EmailChanged(updatedEmailStr))

            // assert
            expectThat(vm.state.value) {
                get { email } isEqualTo EmailAddress.create(updatedEmailStr)
                get { authFailureOrSuccessOption }.isNone()
            }
        }
    }

    context("password is changed") {
        test("password is updated and auth failure option is reset") {
            // arrange
            val updatedPasswordStr = "alnfanfa"

            // act
            vm.handleEvent(AuthPresenter.Event.PasswordChanged(updatedPasswordStr))

            // assert
            expectThat(vm.state.value) {
                get { password } isEqualTo Password.create(updatedPasswordStr)
                get { authFailureOrSuccessOption }.isNone()
            }
        }
    }

    context("sign in with google is pressed") {
        test("google sign in is successful, state changes to loading, then state has auth success") {
            // arrange
            coEvery {
                signInWithTokenUseCaseMock
                    .execute()
            } returns Unit.right()

            vm.state.test {
                awaitItem() // Ignore initial state

                // act
                vm.handleEvent(AuthPresenter.Event.SignInWithGooglePressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo true
                        get { authFailureOrSuccessOption }.isNone()
                    }

                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isSome()
                    }
                }

            }
        }
        test("google sign in is unsuccessful, state changes to loading, then state has auth failure") {
            // arrange
            coEvery {
                signInWithTokenUseCaseMock
                    .execute()
            } returns AuthFailure.ServerError.left()

            vm.state.test {
                awaitItem() // Ignore initial state

                // act
                vm.handleEvent(AuthPresenter.Event.SignInWithGooglePressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo true
                        get { authFailureOrSuccessOption }.isNone()
                    }

                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isSome()
                    }
                }

            }
        }
    }

    context("register with email address and password is pressed") {
        test("email address and password are valid, register using use case and have response in authFailureOrSuccessOption") {
            // arrange
            val validEmailStr = "abc@xyz.com"
            val validPasswordStr = "1234567"

            vm.handleEvent(AuthPresenter.Event.EmailChanged(validEmailStr))
            vm.handleEvent(AuthPresenter.Event.PasswordChanged(validPasswordStr))

            coEvery {
                registerWithEmailAddressAndPasswordUseCaseMock
                    .execute(any(), any())
            } returns Unit.right()

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.RegisterWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo true
                        get { authFailureOrSuccessOption }.isNone()
                    }

                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isSome()
                    }
                }

                coVerify(exactly = 1) {
                    registerWithEmailAddressAndPasswordUseCaseMock
                        .execute(
                            EmailAddress.create(validEmailStr),
                            Password.create(validPasswordStr)
                        )
                }
            }
        }
        test("email address is invalid, start showing error messages and keep authFailureOrSuccess as none") {
            // arrange
            val validPasswordStr = "1234567"
            vm.handleEvent(AuthPresenter.Event.PasswordChanged(validPasswordStr))

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.RegisterWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expectThat(awaitItem()) {
                    get { isSubmitting } isEqualTo false
                    get { authFailureOrSuccessOption }.isNone()
                    get { showErrorMessages } isEqualTo true
                }

                coVerify {
                    registerWithEmailAddressAndPasswordUseCaseMock.execute(
                        any(),
                        any()
                    ) wasNot Called
                }
            }
        }
        test("password is invalid, start showing error messages and keep authFailureOrSuccess as none") {
            // arrange
            val validEmailStr = "abc@xyz.com"
            vm.handleEvent(AuthPresenter.Event.EmailChanged(validEmailStr))

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.RegisterWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isNone()
                        get { showErrorMessages } isEqualTo true
                    }
                }

                coVerify {
                    registerWithEmailAddressAndPasswordUseCaseMock.execute(
                        any(),
                        any()
                    ) wasNot Called
                }
            }

        }
    }
    context("sign in with email address and password is pressed") {
        test("email address and password are valid, sign in using use case and have response in authFailureOrSuccessOption") {
            // arrange
            val validEmailStr = "abc@xyz.com"
            val validPasswordStr = "1234567"

            vm.handleEvent(AuthPresenter.Event.EmailChanged(validEmailStr))
            vm.handleEvent(AuthPresenter.Event.PasswordChanged(validPasswordStr))

            coEvery {
                signInWithEmailAddressAndPasswordUseCaseMock
                    .execute(any(), any())
            } returns Unit.right()

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.SignInWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo true
                        get { authFailureOrSuccessOption }.isNone()
                    }

                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isSome()
                    }
                }

                coVerify(exactly = 1) {
                    signInWithEmailAddressAndPasswordUseCaseMock
                        .execute(
                            EmailAddress.create(validEmailStr),
                            Password.create(validPasswordStr)
                        )
                }
            }
        }
        test("email address is invalid, start showing error messages and keep authFailureOrSuccess as none") {
            // arrange
            val validPasswordStr = "1234567"
            vm.handleEvent(AuthPresenter.Event.PasswordChanged(validPasswordStr))

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.SignInWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expectThat(awaitItem()) {
                    get { isSubmitting } isEqualTo false
                    get { authFailureOrSuccessOption }.isNone()
                    get { showErrorMessages } isEqualTo true
                }

                coVerify {
                    signInWithEmailAddressAndPasswordUseCaseMock.execute(
                        any(),
                        any()
                    ) wasNot Called
                }
            }
        }
        test("password is invalid, start showing error messages and keep authFailureOrSuccess as none") {
            // arrange
            val validEmailStr = "abc@xyz.com"
            vm.handleEvent(AuthPresenter.Event.EmailChanged(validEmailStr))

            vm.state.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(AuthPresenter.Event.SignInWithEmailAndPasswordPressed)
                coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                // assert
                expect {
                    that(awaitItem()) {
                        get { isSubmitting } isEqualTo false
                        get { authFailureOrSuccessOption }.isNone()
                        get { showErrorMessages } isEqualTo true
                    }
                }

                coVerify {
                    signInWithEmailAddressAndPasswordUseCaseMock.execute(
                        any(),
                        any()
                    ) wasNot Called
                }
            }
        }
    }
})
