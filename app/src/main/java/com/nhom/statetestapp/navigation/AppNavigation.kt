package com.nhom.statetestapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nhom.statetestapp.screens.*

/**
 * ============================================================
 * AppNavigation – Điều hướng toàn bộ app
 * Phụ trách: TV5
 * ============================================================
 * Định nghĩa 6 route:
 *   "home"            → HomeScreen
 *   "remember"        → RememberScreen        (TV1 implement)
 *   "rememberSaveable"→ RememberSaveableScreen (TV2 implement)
 *   "viewModel"       → ViewModelScreen        (TV3 implement)
 *   "savedState"      → SavedStateScreen       (TV4 implement)
 *   "dataStore"       → DataStoreScreen        (TV5 implement)
 * ============================================================
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("remember") {
            RememberScreen()
        }
        composable("rememberSaveable") {
            RememberSaveableScreen()
        }
        composable("viewModel") {
            ViewModelScreen()
        }
        composable("savedState") {
            SavedStateScreen()
        }
        composable("dataStore") {
            DataStoreScreen()
        }
    }
}
