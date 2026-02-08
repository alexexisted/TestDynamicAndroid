package com.alexa.testdynamicandroid.feature.wallet.domain.repository

interface WalletRepository {
    suspend fun getEvmWalletInfo(): Result<WalletInfo?>
    suspend fun getEvmBalance(): Result<String>
    suspend fun switchAndGetEvmNetwork(networkId: Long): Result<String>
    suspend fun logout(): Result<Unit>
}

data class WalletInfo(
    val address: String,
    val chain: String
)
