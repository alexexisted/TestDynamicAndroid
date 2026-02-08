package com.alexa.testdynamicandroid.feature.transaction

import com.alexa.testdynamicandroid.feature.wallet.presentation.WalletAction

sealed interface TransactionAction {
    object LoadWallet : TransactionAction
    object OnCopyAddressClicked : TransactionAction
    data class OnDestinationAddressChanged(val newAddress: String) : TransactionAction
    data class OnValueToSendChanged(val newValue: String) : TransactionAction
    object OnSendTransactionClicked : TransactionAction
}