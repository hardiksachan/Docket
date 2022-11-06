package com.hardiksachan.docket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hardiksachan.auth_framework.GoogleSignInCallbackHandler
import com.hardiksachan.auth_framework.GoogleTokenFacade
import com.hardiksachan.core_ui.theme.DocketTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var googleTokenFacade: GoogleTokenFacade
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DocketTheme {
                MyApp()
                GoogleSignInCallbackHandler(googleTokenFacade)
            }
        }
    }
}
