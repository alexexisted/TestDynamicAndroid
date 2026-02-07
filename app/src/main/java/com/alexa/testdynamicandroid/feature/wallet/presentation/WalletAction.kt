package com.alexa.testdynamicandroid.feature.wallet.presentation

sealed interface WalletAction {
    object LoadWallet : WalletAction
    object OnCopyAddressClicked : WalletAction
    object OnSendTransactionClicked : WalletAction
    object OnRefreshDataPulled : WalletAction
    object OnLogoutClicked : WalletAction
    object OnNavigateBackClicked : WalletAction
}