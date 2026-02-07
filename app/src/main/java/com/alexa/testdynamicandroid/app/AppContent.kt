package com.alexa.testdynamicandroid.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexa.testdynamicandroid.feature.auth.presentation.AuthScreen
import com.alexa.testdynamicandroid.feature.wallet.presentation.WalletScreen

@Composable
fun AppContent() {
    val viewModel: SessionViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    when {
        isLoading -> LoadingScreen()
        isAuthenticated -> WalletScreen()
        else -> AuthScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        CircularProgressIndicator()
    }
}