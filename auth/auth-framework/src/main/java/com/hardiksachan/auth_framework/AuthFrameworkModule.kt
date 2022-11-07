package com.hardiksachan.auth_framework

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.TokenFacade
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthFrameworkModule {
    @Binds
    fun bindFirebaseAuthFacade(
        firebaseAuthFacade: FirebaseAuthFacade
    ): AuthFacade

    @Binds
    fun bindGoogleTokenGenerator(
        googleTokenFacade: GoogleTokenFacade
    ): TokenFacade

    @Binds
    fun provideGoogleAuthCredentialProvider(
        googleAuthCredentialProvider: GoogleAuthCredentialProvider
    ): GoogleAuthCredential

    companion object {
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }

        @Provides
        fun provideOneTapClient(
            @ApplicationContext context: Context
        ): SignInClient = Identity.getSignInClient(context)

        @Provides
        fun provideWebServerId(): String = "574697216567-ij3po6fg5b1onfuuvknhu92k2ofmh5fr.apps.googleusercontent.com"
    }
}