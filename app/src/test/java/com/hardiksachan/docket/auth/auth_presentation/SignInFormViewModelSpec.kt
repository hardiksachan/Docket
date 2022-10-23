package com.hardiksachan.docket.auth.auth_presentation

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.hardiksachan.docket.auth.auth_application.SignInWithGoogleUseCase
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import com.hardiksachan.docket.core.MainCoroutineListener
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import strikt.api.expect
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

class FakeSignInWithGoogleUseCase(
    var shouldSucceed: Boolean = true
) : SignInWithGoogleUseCase {
    override suspend fun execute(): Either<AuthFailure, Unit> {
        if (shouldSucceed) {
            return Unit.right()
        }
        return AuthFailure.ServerError.left()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SignInFormViewModelSpec : BehaviorSpec({
    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val signInWithGoogleUseCase = FakeSignInWithGoogleUseCase()
    lateinit var vm: SignInFormViewModel

    beforeTest {
        vm = SignInFormViewModel(signInWithGoogleUseCase)
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
                signInWithGoogleUseCase.shouldSucceed = false
                Then("state changes to loading, then state has auth failure") {
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
    }
})
