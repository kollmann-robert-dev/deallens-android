package com.deallens.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deallens.ui.screens.HomeScreen
import com.deallens.ui.screens.ResultScreen
import com.deallens.ui.screens.ScanScreen
import com.deallens.ui.viewmodel.DealLensViewModel

@Composable
fun DealLensApp(viewModel: DealLensViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onOpenScan = { navController.navigate("scan") },
                onOpenResults = { navController.navigate("results") }
            )
        }
        composable("scan") {
            ScanScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFinished = { navController.navigate("results") }
            )
        }
        composable("results") {
            ResultScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
