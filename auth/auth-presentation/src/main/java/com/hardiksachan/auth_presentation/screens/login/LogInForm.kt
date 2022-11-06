package com.hardiksachan.auth_presentation.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arrow.core.getOrHandle
import arrow.core.none
import arrow.core.some
import com.hardiksachan.auth_application.AuthPresenter
import com.hardiksachan.core.failures.ValueFailure
import com.hardiksachan.core_ui.components.TextFieldWithError

@Composable
internal fun LogInForm(
    uiState: AuthPresenter.State,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    Column {
        TextFieldWithError(
            value = uiState.email.value.getOrHandle { failure -> failure.failedValue },
            onValueChange = onEmailAddressChanged,
            placeholder = {
                Text("alex@gmail.com")
            },
            label = {
                Text("Email Address")
            },
            modifier = Modifier
                .fillMaxWidth(),
            errorMessage = uiState.email.value.fold(
                { failure ->
                    if (uiState.showErrorMessages) {
                        when (failure) {
                            is ValueFailure.InvalidEmail -> "Invalid Email".some()
                            else -> none()
                        }
                    } else {
                        none()
                    }
                },
                { none() }
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextFieldWithError(
            value = uiState.password.value.getOrHandle { failure -> failure.failedValue },
            onValueChange = onPasswordChanged,
            placeholder = {
                Text("at least 8 characters")
            },
            label = {
                Text("Password")
            },
            modifier = Modifier
                .fillMaxWidth(),
            errorMessage = uiState.password.value.fold(
                { failure ->
                    if (uiState.showErrorMessages) {
                        when (failure) {
                            is ValueFailure.ShortPassword -> "Password should be at least 8 characters long".some()
                            else -> none()
                        }
                    } else {
                        none()
                    }
                },
                { none() }
            )
        )
    }
}