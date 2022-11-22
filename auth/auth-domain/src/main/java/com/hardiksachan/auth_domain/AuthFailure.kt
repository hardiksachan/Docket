package com.hardiksachan.auth_domain

sealed class AuthFailure {
    data class TokenNotGenerated(val failure: Token.Failure) : AuthFailure()
    object InvalidToken : AuthFailure()
    object ServerError : AuthFailure()
    object EmailAlreadyInUse : AuthFailure()
    object InvalidEmailAndPasswordCombination : AuthFailure()
}
