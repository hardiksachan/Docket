package com.hardiksachan.docket.ui.auth.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardiksachan.docket.R
import com.hardiksachan.docket.ui.theme.DocketTheme

@Composable
fun StartScreen(
    onGetStartedClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 24.dp,
                top = 0.dp,
                end = 24.dp,
                bottom = 48.dp
            )
    ) {
        Spacer(modifier = Modifier.weight(2f))
        Image(
            painterResource(id = R.drawable.start_sreen_graphic),
            "start screen graphic",
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.weight(2f))
        Text(
            buildAnnotatedString {
                append("Welcome to ")
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.primary)) {
                    append("Docket")
                }
                append("!")

            }, style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Manage tasks of all your university\ncourses in one place",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(3f))
        Button(
            onClick = onGetStartedClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    DocketTheme {
        StartScreen { }
    }
}