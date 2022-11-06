package com.hardiksachan.auth_application

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import com.hardiksachan.auth_application.usecases.RegisterWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.usecases.SignInWithEmailAddressAndPasswordUseCase
import com.hardiksachan.auth_application.usecases.SignInWithTokenUseCase
import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthPresenter(
    private val signInWithGoogleUseCase: SignInWithTokenUseCase,
    private val registerWithEmailAddressAndPasswordUseCase: RegisterWithEmailAddressAndPasswordUseCase,
    private val signInWithEmailAddressAndPasswordUseCase: SignInWithEmailAddressAndPasswordUseCase,
) {
    data class State(
        val email: EmailAddress = EmailAddress.create(""),
        val password: Password = Password.create(""),
        val showErrorMessages: Boolean = false,
        val isSubmitting: Boolean = false,
        val authFailureOrSuccessOption: Option<Either<AuthFailure, Unit>> = None,
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    sealed class Event {
        data class EmailChanged(val email: String) : Event()
        data class PasswordChanged(val password: String) : Event()
        object RegisterWithEmailAndPasswordPressed : Event()
        object SignInWithEmailAndPasswordPressed : Event()
        object SignInWithGooglePressed : Event()
    }

    suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.EmailChanged -> handleEmailChanged(event)
            is Event.PasswordChanged -> handlePasswordChanged(event)
            Event.RegisterWithEmailAndPasswordPressed -> handleRegisterWithEmailAddressAndPasswordPressed()
            Event.SignInWithEmailAndPasswordPressed -> handleSignInWithEmailAddressAndPasswordPressed()
            Event.SignInWithGooglePressed -> handleSignInWithGooglePressed()
        }
    }

    private fun handleEmailChanged(event: Event.EmailChanged) {
        _state.update {
            it.copy(
                email = EmailAddress.create(event.email),
                authFailureOrSuccessOption = None
            )
        }
    }

    private fun handlePasswordChanged(event: Event.PasswordChanged) {
        _state.update {
            it.copy(
                password = Password.create(event.password),
                authFailureOrSuccessOption = None
            )
        }
    }

    private suspend fun handleSignInWithGooglePressed() {
        _state.update {
            it.copy(
                isSubmitting = true,
                authFailureOrSuccessOption = None
            )
        }
        val authFailureOrSuccess = signInWithGoogleUseCase.execute()
        _state.update {
            it.copy(
                isSubmitting = false,
                authFailureOrSuccessOption = authFailureOrSuccess.some()
            )
        }
    }

    private suspend fun handleRegisterWithEmailAddressAndPasswordPressed() =
        registerOrSignInWithEmailAddressAndPassword { email, password ->
            registerWithEmailAddressAndPasswordUseCase.execute(email, password)
        }

    private suspend fun handleSignInWithEmailAddressAndPasswordPressed() =
        registerOrSignInWithEmailAddressAndPassword { email, password ->
            signInWithEmailAddressAndPasswordUseCase.execute(email, password)
        }

    private suspend fun registerOrSignInWithEmailAddressAndPassword(
        useCaseForwardedCall: suspend (EmailAddress, Password) -> Either<AuthFailure, Unit>
    ) {
        val (email, password) = _state.value.run { email to password }

        if (!email.isValid() || !password.isValid()) {
            _state.update {
                it.copy(
                    authFailureOrSuccessOption = None,
                    showErrorMessages = true
                )
            }
            return
        }

        _state.update {
            it.copy(
                isSubmitting = true,
                authFailureOrSuccessOption = None
            )
        }

        val useCaseResponse = useCaseForwardedCall(email, password)

        _state.update {
            it.copy(
                isSubmitting = false,
                authFailureOrSuccessOption = useCaseResponse.some()
            )
        }
    }
}