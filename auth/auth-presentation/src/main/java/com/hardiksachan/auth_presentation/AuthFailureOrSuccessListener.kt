package com.hardiksachan.auth_presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import arrow.core.Either
import arrow.core.Option
import com.hardiksachan.auth_domain.AuthFailure
import kotlinx.coroutines.launch

@Composable
internal fun AuthFailureOrSuccessListener(
    authFailureOrSuccessOption: Option<Either<AuthFailure, Unit>>,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    authFailureOrSuccessOption.fold(
        {},
        {
            it.fold(
                { failure ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(failure.toUserFriendlyMessage())
                    }
                },
                { /*TODO: navigate to user page */ }
            )
        }
    )
}