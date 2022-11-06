package com.hardiksachan.docket

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hardiksachan.auth_presentation.AuthGraphRoutePattern
import com.hardiksachan.auth_presentation.authGraph

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutePattern
    ) {
        authGraph(
            navController = navController
        )
    }
}