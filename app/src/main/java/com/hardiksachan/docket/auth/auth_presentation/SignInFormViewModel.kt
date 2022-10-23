package com.hardiksachan.docket.auth.auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import com.hardiksachan.docket.auth.auth_application.SignInWithGoogleUseCase
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInFormViewModel(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
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
            ViewEvent.RegisterWithEmailAndPasswordPressed -> TODO()
            ViewEvent.SignInWithEmailAndPasswordPressed -> TODO()
            ViewEvent.SignInWithGooglePressed -> handleSignInWithGoogle()
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

    private fun handleSignInWithGoogle() {
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
}