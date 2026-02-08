package com.alexa.testdynamicandroid.feature.transaction.domain.repository

import com.dynamic.sdk.android.Models.BaseWallet

interface TransactionRepository {
    suspend fun getEvmWalletAddress(): Result<String>
    suspend fun getEvmBalance(): Result<String>
    suspend fun getEvmWallet(): Result<BaseWallet>
    suspend fun sendEvmTransaction(
        fromAddress: String,
        destinationAddress: String,
        amount: String,
        baseWallet: BaseWallet
    ): Result<String>
}
