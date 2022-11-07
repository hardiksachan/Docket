package com.hardiksachan.auth_presentation

import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.Token

fun AuthFailure.toUserFriendlyMessage() = when (this) {
    AuthFailure.EmailAlreadyInUse -> "Email already exists"
    AuthFailure.InvalidEmailAndPasswordCombination -> "Invalid email and password"
    AuthFailure.InvalidToken -> "Invalid token found. Please try again"
    AuthFailure.ServerError -> "An unknown server error occurred"
    is AuthFailure.TokenNotGenerated -> failure.toUserFriendlyMessage()
}

fun Token.GenerationFailure.toUserFriendlyMessage() = when (this) {
    Token.GenerationFailure.NoAccountsFound -> "No accounts found"
    Token.GenerationFailure.PromptDismissedByUser -> "Cancelled by user"
    Token.GenerationFailure.ServerError -> "An unknown server error occurred"
    Token.GenerationFailure.UnableToLaunchPrompt -> "Unable to launch prompt"
    Token.GenerationFailure.UnknownFailure -> "An unknown error occurred"
}