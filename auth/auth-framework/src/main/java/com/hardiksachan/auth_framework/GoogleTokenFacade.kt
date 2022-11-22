package com.hardiksachan.auth_framework

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.hardiksachan.auth_domain.Token
import com.hardiksachan.auth_domain.TokenFacade
import com.hardiksachan.core.flatMapLeft
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class GoogleTokenFacade @Inject constructor(
    private val oneTapClient: SignInClient,
    private val webServerId: String,
) : TokenFacade {
    private val intentChannel = Channel<Intent?>()
    var launcher: ActivityResultLauncher<IntentSenderRequest>? = null

    override suspend fun generate(): Either<Token.Failure, Token> {
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
                    Token.Failure.NoAccountsFound.left()
                },
                { result ->
                    try {
                        launcher?.launch(
                            IntentSenderRequest.Builder(
                                result.pendingIntent.intentSender
                            ).build()
                        ) ?: return Token.Failure.UnknownFailure.left()

                        val credential = try {
                            oneTapClient
                                .getSignInCredentialFromIntent(intentChannel.receive())
                                .right()
                        } catch (exception: ApiException) {
                            exception.left()
                        }

                        credential.toToken()
                    } catch (e: Exception) {
                        Token.Failure.UnableToLaunchPrompt.left()
                    }
                })
    }

    override suspend fun invalidate(): Either<Token.Failure, Unit> =
        when (oneTapClient.signOut().awaitCompletion()) {
            is Either.Left -> Token.Failure.ServerError.left()
            is Either.Right -> Unit.right()
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
                Token.Failure.PromptDismissedByUser
            }
            CommonStatusCodes.NETWORK_ERROR -> {
                Token.Failure.ServerError
            }
            else -> {
                Token.Failure.UnknownFailure
            }
        }.left()
    }, { r ->
        r.googleIdToken?.let {
            Token.Google(it).right()
        } ?: Token.Failure.UnknownFailure.left()
    })

    internal suspend fun onResultFromView(intent: Intent?) {
        intentChannel.send(intent)
    }
}

suspend fun <T> Task<T>.awaitCompletion() = suspendCoroutine<Either<Exception?, T>> { cont ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            cont.resume(task.result.right())
        } else {
            cont.resume(task.exception.left())
        }
    }
}
