package com.hardiksachan.docket.auth.auth_presentation

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.hardiksachan.docket.auth.auth_application.RegisterWithEmailAddressAndPasswordUseCase
import com.hardiksachan.docket.auth.auth_application.SignInWithEmailAddressAndPasswordUseCase
import com.hardiksachan.docket.auth.auth_application.SignInWithGoogleUseCase
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import com.hardiksachan.docket.core.MainCoroutineListener
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
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
class SignInFormViewModelSpec : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val signInWithGoogleUseCaseMock =
        mockk<SignInWithGoogleUseCase>()
    val registerWithEmailAddressAndPasswordUseCaseMock =
        mockk<RegisterWithEmailAddressAndPasswordUseCase>()
    val signInWithEmailAddressAndPasswordUseCaseMock =
        mockk<SignInWithEmailAddressAndPasswordUseCase>()

    lateinit var vm: SignInFormViewModel

    beforeTest {
        vm = SignInFormViewModel(
            signInWithGoogleUseCaseMock,
            registerWithEmailAddressAndPasswordUseCaseMock,
            signInWithEmailAddressAndPasswordUseCaseMock
        )
    }

    Given("view model") {

        When("email address is changed") {
            Then("email address is updated and auth failure option is reset") {
                val updatedEmailStr = "abc@xyz.com"
                vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(updatedEmailStr))

                expectThat(vm.viewState.value) {
                    get { email } isEqualTo EmailAddress.create(updatedEmailStr)
                    get { authFailureOrSuccessOption }.isNone()
                }
            }
        }

        When("password is changed") {
            Then("password is updated and auth failure option is reset") {
                val updatedPasswordStr = "alnfanfa"
                vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(updatedPasswordStr))

                expectThat(vm.viewState.value) {
                    get { password } isEqualTo Password.create(updatedPasswordStr)
                    get { authFailureOrSuccessOption }.isNone()
                }
            }
        }

        When("sign in with google is pressed") {
            And("google sign in is successful") {
                Then("state changes to loading, then state has auth success") {

                    coEvery {
                        signInWithGoogleUseCaseMock
                            .execute()
                    } returns Unit.right()

                    vm.viewState.test {
                        awaitItem() // Ignore initial state

                        vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithGooglePressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
            And("google sign in is unsuccessful") {
                Then("state changes to loading, then state has auth failure") {

                    coEvery {
                        signInWithGoogleUseCaseMock
                            .execute()
                    } returns AuthFailure.ServerError.left()

                    vm.viewState.test {
                        awaitItem() // Ignore initial state

                        vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithGooglePressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
        }

        When("register with email address and password is pressed") {
            And("email address and password are valid") {
                Then("register using use case and have response in authFailureOrSuccessOption") {
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

                        vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
            }
            And("email address is invalid") {
                Then("start showing error messages and keep authFailureOrSuccess as none") {
                    val validPasswordStr = "1234567"

                    vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

                    vm.viewState.test {
                        awaitItem() // Ignore Initial State

                        vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                        expectThat(awaitItem()) {
                            get { isSubmitting } isEqualTo false
                            get { authFailureOrSuccessOption }.isNone()
                            get { showErrorMessages } isEqualTo true
                        }

                        coVerify {
                            registerWithEmailAddressAndPasswordUseCaseMock.execute(any(), any()) wasNot Called
                        }
                    }
                }
            }
            And("password is invalid") {
                Then("start showing error messages and keep authFailureOrSuccess as none") {
                    val validEmailStr = "abc@xyz.com"

                    vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))

                    vm.viewState.test {
                        awaitItem() // Ignore Initial State

                        vm.handleEvent(SignInFormViewModel.ViewEvent.RegisterWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

                        expect {
                            that(awaitItem()) {
                                get { isSubmitting } isEqualTo false
                                get { authFailureOrSuccessOption }.isNone()
                                get { showErrorMessages } isEqualTo true
                            }
                        }

                        coVerify {
                            registerWithEmailAddressAndPasswordUseCaseMock.execute(any(), any()) wasNot Called
                        }
                    }
                }
            }
        }

        When("sign in with email address and password is pressed") {
            And("email address and password are valid") {
                Then("sign in using use case and have response in authFailureOrSuccessOption") {
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

                        vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
            }
            And("email address is invalid") {
                Then("start showing error messages and keep authFailureOrSuccess as none") {
                    val validPasswordStr = "1234567"

                    vm.handleEvent(SignInFormViewModel.ViewEvent.PasswordChanged(validPasswordStr))

                    vm.viewState.test {
                        awaitItem() // Ignore Initial State

                        vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
            }
            And("password is invalid") {
                Then("start showing error messages and keep authFailureOrSuccess as none") {
                    val validEmailStr = "abc@xyz.com"

                    vm.handleEvent(SignInFormViewModel.ViewEvent.EmailChanged(validEmailStr))

                    vm.viewState.test {
                        awaitItem() // Ignore Initial State

                        vm.handleEvent(SignInFormViewModel.ViewEvent.SignInWithEmailAndPasswordPressed)
                        coroutineListener.testDispatcher.scheduler.advanceUntilIdle()

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
        }
    }
})
