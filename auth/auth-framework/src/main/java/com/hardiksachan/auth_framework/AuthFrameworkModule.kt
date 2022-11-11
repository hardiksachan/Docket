package com.hardiksachan.auth_framework

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.hardiksachan.auth_domain.AuthFacade
import com.hardiksachan.auth_domain.TokenFacade
import com.hardiksachan.auth_framework.constants.PrivateConstants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App

@Module
@InstallIn(SingletonComponent::class)
interface AuthFrameworkModule {
    @Binds
    fun bindRealmAuthFacade(
        realmAuthFacade: RealmAuthFacade
    ): AuthFacade

    @Binds
    fun bindGoogleTokenGenerator(
        googleTokenFacade: GoogleTokenFacade
    ): TokenFacade

    companion object {
        @Provides
        fun provideRealmApp(): App = App.create(PrivateConstants.realmAppId)

        @Provides
        fun provideOneTapClient(
            @ApplicationContext context: Context
        ): SignInClient = Identity.getSignInClient(context)

        @Provides
        fun provideWebServerId(): String = PrivateConstants.googleWebServerId
    }
}