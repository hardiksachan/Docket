package com.hardiksachan.auth_presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hardiksachan.core_ui.theme.Grey
import com.hardiksachan.core_ui.components.StrokedRow
import com.hardiksachan.core_ui.resources.Icons

@Composable
internal fun SocialLogin(onGoogleLoginPressed: () -> Unit) {
    Column {
        StrokedRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "or continue with",
                style = MaterialTheme.typography.labelMedium,
                color = Grey.Dark
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = onGoogleLoginPressed,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = Icons.google),
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart),
                    contentDescription = "drawable icons",
                    tint = Color.Unspecified
                )
                Text(
                    text = "Google",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
