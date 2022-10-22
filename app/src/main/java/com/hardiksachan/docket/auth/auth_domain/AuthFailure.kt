package com.hardiksachan.docket.auth.auth_domain

sealed class AuthFailure {
    object CancelledByUser: AuthFailure()
    object ServerError: AuthFailure()
    object EmailAlreadyInUse: AuthFailure()
    object InvalidEmailAndPasswordCombination: AuthFailure()
}
