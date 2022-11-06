package com.hardiksachan.auth_framework

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInCallbackHandler(
    googleTokenFacade: GoogleTokenFacade
) {
    val coroutineScope = rememberCoroutineScope()
    googleTokenFacade.launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        coroutineScope.launch {
            googleTokenFacade.onResultFromView(result.data)
        }
    }
}