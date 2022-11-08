package com.hardiksachan.auth_presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.hardiksachan.auth_presentation.screens.login.logInScreen
import com.hardiksachan.auth_presentation.screens.login.navigateToLogin
import com.hardiksachan.auth_presentation.screens.signup.navigateToSignUp
import com.hardiksachan.auth_presentation.screens.signup.signUpScreen
import com.hardiksachan.auth_presentation.screens.start.StartRoutePattern
import com.hardiksachan.auth_presentation.screens.start.startScreen

const val AuthGraphRoutePattern = "auth"

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    navigation(
        startDestination = StartRoutePattern,
        route = AuthGraphRoutePattern
    ) {
        startScreen(
            navigateToLoginPage = { navController.navigateToLogin() }
        )
        logInScreen(
            navigateToSignUpPage = { navController.navigateToSignUp() }
        )
        signUpScreen(
            navigateToLoginPage = { navController.navigateToLogin() }
        )
    }
}