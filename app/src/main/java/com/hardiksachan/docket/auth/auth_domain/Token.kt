package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Option
import arrow.core.none

sealed class Token {
    data class Google(val idToken: String, val accessToken: Option<String> = none()): Token()

    sealed class GenerationFailure {
        object UnknownFailure: GenerationFailure()
        object ServerError: GenerationFailure()
        object NoAccountsFound: GenerationFailure()
        object UnableToLaunchPrompt: GenerationFailure()
        object PromptDismissedByUser: GenerationFailure()
    }
}