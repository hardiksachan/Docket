package com.hardiksachan.auth_domain

import arrow.core.Option
import arrow.core.none

sealed class Token {
    data class Google(val idToken: String, val accessToken: Option<String> = none()): Token()

    sealed class Failure {
        object UnknownFailure: Failure()
        object ServerError: Failure()
        object NoAccountsFound: Failure()
        object UnableToLaunchPrompt: Failure()
        object PromptDismissedByUser: Failure()
    }
}