package com.example.banca


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.banca.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    navController.navigate("main/$role") { popUpTo("login") { inclusive = true } }
                }
            )
        }
        composable(
            route = "main/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val userRole = backStackEntry.arguments?.getString("role") ?: "LISTERO"
            MainScreen(
                userRole = userRole,
                onNavigateToLimits = { navController.navigate("limits") },
                onNavigateToVault = { navController.navigate("vault") },
                onNavigateToBankVault = { navController.navigate("bank_vault") },
                onNavigateToLists = { navController.navigate("lists") }
            )
        }
        composable("vault") { VaultScreen(onBack = { navController.popBackStack() }) }
        composable("limits") { LimitsScreen(onBack = { navController.popBackStack() }) }
        composable("bank_vault") { BankScreen(onBack = { navController.popBackStack() }) }
        composable("lists") {
            ListSummaryScreen(
                onNavigateToDetail = { listId, selectedDate ->
                    navController.navigate("list_detail/$listId/$selectedDate")
                }
            )
        }
        composable("list_detail/{listId}/{selectedDate}") { backStackEntry ->
            val listId =
                backStackEntry.arguments?.getString("listId")?.toLong() ?: 0L

            val selectedDate =
                backStackEntry.arguments?.getString("selectedDate")?.toLong() ?: 0L

            ListDetailScreen(
                listId = listId,
                selectedDate = selectedDate
            )
        }

    }

}