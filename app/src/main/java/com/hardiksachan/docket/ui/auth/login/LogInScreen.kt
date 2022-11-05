package com.hardiksachan.docket.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import com.hardiksachan.auth_presentation.SignInFormViewModel
import com.hardiksachan.docket.ui.auth.LogInForm
import com.hardiksachan.docket.ui.auth.SocialLogin
import com.hardiksachan.docket.ui.theme.DocketTheme
import com.hardiksachan.docket.ui.theme.Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    uiState: SignInFormViewModel.ViewState,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginButtonPressed: () -> Unit,
    onSignUpButtonPressed: () -> Unit,
    onGoogleLoginPressed: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Log In", style = MaterialTheme.typography.displayMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = Grey.Medium
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            LogInForm(
                uiState = uiState,
                onEmailAddressChanged = onEmailAddressChanged,
                onPasswordChanged = onPasswordChanged
            )
            Spacer(modifier = Modifier.weight(1f))
            LoginFormButtons(onLoginButtonPressed, onSignUpButtonPressed)
            Spacer(modifier = Modifier.height(16.dp))
            SocialLogin(onGoogleLoginPressed)
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun LoginFormButtons(
    onLoginButtonPressed: () -> Unit,
    onSignUpButtonPressed: () -> Unit
) {
    Button(
        onClick = onLoginButtonPressed,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            "Log In",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    TextButton(
        onClick = onSignUpButtonPressed,
    ) {
        Text(
            buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sign Up")
                }
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun LogInScreenPreview() {
    DocketTheme {
        LogInScreen(
            uiState = SignInFormViewModel.ViewState(),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onLoginButtonPressed = {},
            onSignUpButtonPressed = {},
            onGoogleLoginPressed = {}
        )
    }
}

@Preview
@Composable
fun LogInScreenPreviewWithError() {
    DocketTheme {
        LogInScreen(
            uiState = SignInFormViewModel.ViewState(
                email = EmailAddress.create("ab"),
                password = Password.create("dgf"),
                showErrorMessages = true,
            ),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onLoginButtonPressed = {},
            onSignUpButtonPressed = {},
            onGoogleLoginPressed = {}
        )
    }
}