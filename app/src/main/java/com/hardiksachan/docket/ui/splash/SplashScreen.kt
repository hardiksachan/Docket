package com.hardiksachan.docket.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hardiksachan.core_ui.resources.Graphics
import com.hardiksachan.core_ui.theme.DocketTheme

@Composable
fun SplashScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Image(painterResource(id = Graphics.startScreen), "logo")
        Spacer(modifier = Modifier.weight(4f))
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    DocketTheme {
        SplashScreen()
    }
}