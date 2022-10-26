package com.hardiksachan.docket.auth.auth_presentation

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.hardiksachan.docket.auth.auth_application.RegisterWithEmailAddressAndPasswordUseCase
import com.hardiksachan.docket.auth.auth_application.SignInWithEmailAddressAndPasswordUseCase
import com.hardiksachan.docket.auth.auth_application.SignInWithTokenUseCase
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import com.hardiksachan.docket.core.MainCoroutineListener
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
class SignInFormViewModelSpec : FunSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val signInWithTokenUseCaseMock =
        mockk<SignInWithTokenUseCase>()
    val registerWithEmailAddressAndPasswordUseCaseMock =
        mockk<RegisterWithEmailAddressAndPasswordUseCase>()
    val signInWithEmailAddressAndPasswordUseCaseMock =
        mockk<SignInWithEmailAddressAndPasswordUseCase>()

    lateinit var vm: SignInFormViewModel

    beforeTest {
        vm = SignInFormViewModel(
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(updatedEmailStr))

            // assert
            expectThat(vm.viewState.value) {
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(updatedPasswordStr))

            // assert
            expectThat(vm.viewState.value) {
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

            vm.viewState.test {
                awaitItem() // Ignore initial state

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithGooglePressed)
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

            vm.viewState.test {
                awaitItem() // Ignore initial state

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithGooglePressed)
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

            vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))
            vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

            coEvery {
                registerWithEmailAddressAndPasswordUseCaseMock
                    .execute(any(), any())
            } returns Unit.right()

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
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

            vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))
            vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

            coEvery {
                signInWithEmailAddressAndPasswordUseCaseMock
                    .execute(any(), any())
            } returns Unit.right()

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
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
            vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))

            vm.viewState.test {
                awaitItem() // Ignore Initial State

                // act
                vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
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
