package com.alexa.testdynamicandroid.feature.wallet.presentation

sealed interface WalletAction {
    object LoadWallet : WalletAction
    object OnCopyAddressClicked : WalletAction
    object OnRefreshDataPulled : WalletAction
    object OnLogoutClicked : WalletAction
}