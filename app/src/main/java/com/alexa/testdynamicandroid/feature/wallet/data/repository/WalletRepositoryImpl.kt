package com.alexa.testdynamicandroid.feature.wallet.data.repository

import com.alexa.testdynamicandroid.feature.wallet.domain.repository.WalletInfo
import com.alexa.testdynamicandroid.feature.wallet.domain.repository.WalletRepository
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.Network
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val sdk: DynamicSDK
) : WalletRepository {

    override suspend fun getEvmWalletInfo(): Result<WalletInfo?> {
        return try {
            val wallet = sdk.wallets.userWallets.firstOrNull { it.chain == "EVM" }
            val info = wallet?.let {
                WalletInfo(
                    address = it.address,
                    chain = it.chain
                )
            }
            Result.success(info)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEvmBalance(): Result<String> {
        return try {
            val wallet = sdk.wallets.userWallets.firstOrNull { it.chain == "EVM" }
            if (wallet != null) {
                val balance = sdk.wallets.getBalance(wallet)
                Result.success("$balance ETH")
            } else {
                Result.failure(Exception("No EVM wallet found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun switchAndGetEvmNetwork(networkId: Long): Result<String> {
        return try {
            val wallet = sdk.wallets.userWallets.first { it.chain == "EVM" }
            val network = Network(JsonPrimitive(networkId))
            sdk.wallets.switchNetwork(wallet, network)
            val networkResult = sdk.wallets.getNetwork(wallet)
            Result.success(networkResult.value.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            sdk.auth.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
