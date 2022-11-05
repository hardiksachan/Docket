package com.hardiksachan.docket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hardiksachan.docket.ui.splash.SplashScreen
import com.hardiksachan.docket.ui.theme.DocketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DocketTheme {
                SplashScreen()
            }
        }
    }
}
