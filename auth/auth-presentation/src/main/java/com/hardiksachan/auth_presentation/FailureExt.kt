package com.hardiksachan.auth_presentation

import com.hardiksachan.auth_domain.AuthFailure
import com.hardiksachan.auth_domain.Token

fun AuthFailure.toUserFriendlyMessage() = when (this) {
    AuthFailure.EmailAlreadyInUse -> "Email already exists"
    AuthFailure.InvalidEmailAndPasswordCombination -> "Invalid email or password"
    AuthFailure.InvalidToken -> "Invalid token found. Please try again"
    AuthFailure.ServerError -> "An unknown server error occurred"
    is AuthFailure.TokenNotGenerated -> failure.toUserFriendlyMessage()
}

fun Token.Failure.toUserFriendlyMessage() = when (this) {
    Token.Failure.NoAccountsFound -> "No accounts found"
    Token.Failure.PromptDismissedByUser -> "Cancelled by user"
    Token.Failure.ServerError -> "An unknown server error occurred"
    Token.Failure.UnableToLaunchPrompt -> "Unable to launch prompt"
    Token.Failure.UnknownFailure -> "An unknown error occurred"
}