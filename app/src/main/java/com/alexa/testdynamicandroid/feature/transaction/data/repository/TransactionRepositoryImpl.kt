package com.alexa.testdynamicandroid.feature.transaction.data.repository

import com.alexa.testdynamicandroid.feature.transaction.domain.repository.TransactionRepository
import com.alexa.testdynamicandroid.feature.wallet.domain.repository.WalletInfo
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.BaseWallet
import java.math.BigInteger
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val sdk: DynamicSDK
) : TransactionRepository {

    override suspend fun getEvmWalletAddress(): Result<String> {
        return try {
            val wallet = sdk.wallets.userWallets.firstOrNull { it.chain == "EVM" }
            if (wallet != null) {
                Result.success(wallet.address)
            } else {
                Result.failure(Exception("No EVM wallet found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun getEvmBalance(): Result<String> {
        return try {
            val wallet = sdk.wallets.userWallets.firstOrNull { it.chain == "EVM" }
            if (wallet != null) {
                val balance = sdk.wallets.getBalance(wallet)
                Result.success(balance)
            } else {
                Result.failure(Exception("No EVM wallet found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEvmWallet(): Result<BaseWallet> {
        return try {
            val evmWallet = sdk.wallets.userWallets.find { it.chain == "EVM" }
            if (evmWallet != null) {
                Result.success(evmWallet)
            } else {
                Result.failure(Exception("No connected EVM wallet found in the current session."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendEvmTransaction(
        fromAddress: String,
        destinationAddress: String,
        amount: String,
        baseWallet: BaseWallet
    ): Result<String> {

        return try {
            val chainId = 11155111
            val client = sdk.evm.createPublicClient(chainId)
            val gasPrice = client.getGasPrice()
            val maxFeePerGas = gasPrice * BigInteger.valueOf(2)
            val weiAmount = convertEthToWei(amount)

            val transaction = EthereumTransaction(
                from = fromAddress,
                to = destinationAddress,
                value = weiAmount,
                gas = BigInteger.valueOf(21000),
                maxFeePerGas = maxFeePerGas,
                maxPriorityFeePerGas = gasPrice
            )
            val txHash = sdk.evm.sendTransaction(transaction, baseWallet)
            Result.success(txHash)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
