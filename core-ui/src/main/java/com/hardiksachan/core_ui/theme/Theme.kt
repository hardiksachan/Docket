package com.hardiksachan.core_ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DocketColorScheme = lightColorScheme(
    primary = Blue.Medium,
    onPrimary = White,
    secondary = Blue.Dark,
    onSecondary = White,
    error = Error,
    onError = White,
    background = Background,
    onBackground = Blue.Dark,
    surface = Grey.Medium,
    onSurface = Blue.Dark,
    outline = Grey.Dark
)


@Composable
fun DocketTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DocketColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}