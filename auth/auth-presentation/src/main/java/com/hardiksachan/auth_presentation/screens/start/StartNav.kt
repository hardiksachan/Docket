package com.hardiksachan.auth_presentation.screens.start

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val StartRoutePattern = "start"

fun NavGraphBuilder.startScreen(
    navigateToLoginPage: () -> Unit
) {
    composable(StartRoutePattern) {
        StartScreen(
            onGetStartedClicked = navigateToLoginPage
        )
    }
}
