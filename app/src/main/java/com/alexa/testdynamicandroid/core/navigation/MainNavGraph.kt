package com.alexa.testdynamicandroid.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexa.testdynamicandroid.core.ui.LoadingScreen
import com.alexa.testdynamicandroid.core.ui.SessionViewModel
import com.alexa.testdynamicandroid.feature.auth.presentation.AuthScreen
import com.alexa.testdynamicandroid.feature.transaction.TransactionScreen
import com.alexa.testdynamicandroid.feature.wallet.presentation.WalletScreen
import kotlinx.serialization.Serializable


@Serializable
sealed interface MainRoute {
    @Serializable
    data object WalletRoute : MainRoute

    @Serializable
    data object TransactionRoute : MainRoute

    @Serializable
    data object LoadingRoute : MainRoute

    @Serializable
    data object AuthRoute : MainRoute
}


@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = hiltViewModel()

    val isLoading by sessionViewModel.isLoading.collectAsState()
    val isAuthenticated by sessionViewModel.isAuthenticated.collectAsState()
    val wallets by sessionViewModel.wallets.collectAsState()

    LaunchedEffect(isLoading, isAuthenticated, wallets) {
        when {
            isLoading -> {
                navController.navigate(MainRoute.LoadingRoute) {
                    popUpTo(0)
                }
            }

            !isAuthenticated -> {
                navController.navigate(MainRoute.AuthRoute) {
                    popUpTo(0)
                }
            }

            else -> {
                navController.navigate(MainRoute.WalletRoute) {
                    popUpTo(0)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute.LoadingRoute
    ) {

        composable<MainRoute.WalletRoute> {
            WalletScreen(
                onNavigateToTransaction = { navController.navigate(MainRoute.TransactionRoute) }
            )
        }

        composable<MainRoute.TransactionRoute> {
            TransactionScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable<MainRoute.LoadingRoute> {
            LoadingScreen()
        }

        composable<MainRoute.AuthRoute> {
            AuthScreen()
        }
    }
}
