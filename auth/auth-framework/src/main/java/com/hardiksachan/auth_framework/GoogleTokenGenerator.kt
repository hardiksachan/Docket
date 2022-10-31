package com.hardiksachan.auth_framework

import android.content.Intent
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.hardiksachan.auth_domain.Token
import com.hardiksachan.auth_domain.TokenGeneratorFacade
import com.hardiksachan.core.flatMapLeft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GoogleTokenGenerator(
    activityResultCaller: ActivityResultCaller,
    private val oneTapClient: SignInClient,
    private val webServerId: String,
    private val dispatcherProvider: com.hardiksachan.core.DispatcherProvider
) : TokenGeneratorFacade, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcherProvider.provideUIContext()

    private val oneTapSignInFlowLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val intentChannel = Channel<Intent?>()

    init {
        oneTapSignInFlowLauncher = activityResultCaller.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            launch {
                intentChannel.send(result.data)
            }
        }
    }

    override suspend fun generate(): Either<Token.GenerationFailure, Token> {
        val signUpRequest = buildRequest(false)
        val signInRequest = buildRequest(true)

        return oneTapClient
            .beginSignIn(signInRequest)
            .awaitCompletion()
            .flatMapLeft {
                oneTapClient
                    .beginSignIn(signUpRequest)
                    .awaitCompletion()
            }.fold(
                {
                    Token.GenerationFailure.NoAccountsFound.left()
                },
                { result ->
                    try {
                        oneTapSignInFlowLauncher.launch(
                            IntentSenderRequest.Builder(
                                result.pendingIntent.intentSender
                            ).build()
                        )

                        val credential = try {
                            oneTapClient
                                .getSignInCredentialFromIntent(intentChannel.receive())
                                .right()
                        } catch (exception: ApiException) {
                            exception.left()
                        }

                        credential.toToken()
                    } catch (e: Exception) {
                        Token.GenerationFailure.UnableToLaunchPrompt.left()
                    }
                })
    }

    private fun buildRequest(filterByAuthorizedAccounts: Boolean) =
        BeginSignInRequest
            .builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions
                    .builder()
                    .setSupported(true)
                    .setServerClientId(webServerId)
                    .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
                    .build()
            ).build()

    private fun Either<ApiException, SignInCredential>.toToken() = this.fold({ exception ->
        when (exception.statusCode) {
            CommonStatusCodes.CANCELED -> {
                Token.GenerationFailure.PromptDismissedByUser
            }
            CommonStatusCodes.NETWORK_ERROR -> {
                Token.GenerationFailure.ServerError
            }
            else -> {
                Token.GenerationFailure.UnknownFailure
            }
        }.left()
    }, { r ->
        r.googleIdToken?.let {
            Token.Google(it).right()
        } ?: Token.GenerationFailure.UnknownFailure.left()
    })
}
