package com.hardiksachan.docket.auth.auth_domain

sealed class Token {
    data class Google(val idToken: String, val accessToken: String): Token()
}