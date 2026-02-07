package com.alexa.testdynamicandroid.feature.wallet.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WalletBalanceComponent(balance: String) {
    Text(
        text = "Balance",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
    Text(
        text = balance,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}
