package com.hardiksachan.auth_framework

import arrow.core.Option
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

fun interface GoogleAuthCredential {
    operator fun invoke(idToken: String, accessToken: Option<String>): AuthCredential
}

@Singleton
class GoogleAuthCredentialProvider @Inject constructor() : GoogleAuthCredential {
    override fun invoke(idToken: String, accessToken: Option<String>): AuthCredential =
        GoogleAuthProvider.getCredential(idToken, accessToken.orNull())
}