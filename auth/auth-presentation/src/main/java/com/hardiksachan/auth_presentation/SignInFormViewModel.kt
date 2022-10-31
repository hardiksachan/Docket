package com.hardiksachan.auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import com.hardiksachan.auth_application.RegisterWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.SignInWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.SignInWithTokenUseCase
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInFormViewModel(
    private val signInWithGoogleUseCase: SignInWithTokenUseCase,
    private val registerWithEmailAddressAndPasswordUseCase: RegisterWithEmailAddressAndPasswordUseCase,
    private val signInWithEmailAddressAndPasswordUseCase: SignInWithEmailAddressAndPasswordUseCase,
) : ViewModel() {
    data class ViewState(
        val email: EmailAddress = EmailAddress.create(""),
        val password: Password = Password.create(""),
        val showErrorMessages: Boolean = false,
        val isSubmitting: Boolean = false,
        val authFailureOrSuccessOption: Option<Either<AuthFailure, Unit>> = None,
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    sealed class ViewEvent {
        data class EmailChanged(val email: String) : ViewEvent()
        data class PasswordChanged(val password: String) : ViewEvent()
        object RegisterWithEmailAndPasswordPressed : ViewEvent()
        object SignInWithEmailAndPasswordPressed : ViewEvent()
        object SignInWithGooglePressed : ViewEvent()
    }

    fun handleEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.EmailChanged -> handleEmailChanged(event)
            is ViewEvent.PasswordChanged -> handlePasswordChanged(event)
            ViewEvent.RegisterWithEmailAndPasswordPressed -> handleRegisterWithEmailAddressAndPasswordPressed()
            ViewEvent.SignInWithEmailAndPasswordPressed -> handleSignInWithEmailAddressAndPasswordPressed()
            ViewEvent.SignInWithGooglePressed -> handleSignInWithGooglePressed()
        }
    }

    private fun handleEmailChanged(event: ViewEvent.EmailChanged) {
        _viewState.update {
            it.copy(
                email = EmailAddress.create(event.email),
                authFailureOrSuccessOption = None
            )
        }
    }

    private fun handlePasswordChanged(event: ViewEvent.PasswordChanged) {
        _viewState.update {
            it.copy(
                password = Password.create(event.password),
                authFailureOrSuccessOption = None
            )
        }
    }

    private fun handleSignInWithGooglePressed() {
        _viewState.update {
            it.copy(
                isSubmitting = true,
                authFailureOrSuccessOption = None
            )
        }
        viewModelScope.launch {
            val authFailureOrSuccess = signInWithGoogleUseCase.execute()
            _viewState.update {
                it.copy(
                    isSubmitting = false,
                    authFailureOrSuccessOption = authFailureOrSuccess.some()
                )
            }
        }
    }

    private fun handleRegisterWithEmailAddressAndPasswordPressed() =
        registerOrSignInWithEmailAddressAndPassword { email, password ->
            registerWithEmailAddressAndPasswordUseCase.execute(email, password)
        }

    private fun handleSignInWithEmailAddressAndPasswordPressed() =
        registerOrSignInWithEmailAddressAndPassword { email, password ->
            signInWithEmailAddressAndPasswordUseCase.execute(email, password)
        }

    private fun registerOrSignInWithEmailAddressAndPassword(
        useCaseForwardedCall: suspend (EmailAddress, Password) -> Either<AuthFailure, Unit>
    ) {
        val (email, password) = _viewState.value.run { email to password }

        if (!email.isValid() || !password.isValid()) {
            _viewState.update {
                it.copy(
                    authFailureOrSuccessOption = None,
                    showErrorMessages = true
                )
            }
            return
        }

        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    isSubmitting = true,
                    authFailureOrSuccessOption = None
                )
            }

            val useCaseResponse = useCaseForwardedCall(email, password)

            _viewState.update {
                it.copy(
                    isSubmitting = false,
                    authFailureOrSuccessOption = useCaseResponse.some()
                )
            }
        }


    }
}