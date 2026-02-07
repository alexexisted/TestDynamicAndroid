package com.alexa.testdynamicandroid.feature.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor() : ViewModel() {
    private val sdk = DynamicSDK.getInstance()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _network = MutableStateFlow<Network?>(null)
    val network: StateFlow<Network?> = _network.asStateFlow()

    private val _network2 = MutableStateFlow<String?>(null)
    val network2: StateFlow<String?> = _network2.asStateFlow()

    private val _balance = MutableStateFlow("")
    val balance: StateFlow<String> = _balance.asStateFlow()

    private val _wallet = MutableStateFlow<String>("")
    val wallet: StateFlow<String> = _wallet.asStateFlow()

    private val _chain = MutableStateFlow("")
    val chain: StateFlow<String> = _chain.asStateFlow()

    private val _uiEventCopyAddress = MutableSharedFlow<String>()
    val uiEventCopyAddress = _uiEventCopyAddress.asSharedFlow()

    init {
        loadWallet()
    }

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.LoadWallet -> loadWallet()
            WalletAction.OnCopyAddressClicked -> copyAddress()
            WalletAction.OnSendTransactionClicked -> sendTransaction()
            WalletAction.OnLogoutClicked -> logout()
            WalletAction.OnNavigateBackClicked -> navigateBack()
            WalletAction.OnRefreshDataPulled -> refreshData()
        }
    }

    private fun copyAddress() {
        viewModelScope.launch {
            _uiEventCopyAddress.emit(address.value)
        }
    }

    private fun loadBalance() {
        viewModelScope.launch {
            try {
                val wallet = sdk.wallets.userWallets.firstOrNull() { it.chain == "EVM" }
                if (wallet == null) {
                    return@launch
                } else {
                    val solBalance = sdk.wallets.getBalance(wallet)
                    _balance.value = "$solBalance ETH"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load balance: ${e.message}"
            }
        }
    }
//GenericNetwork(blockExplorerUrls=[https://sepolia.etherscan.io, https://sepolia.otterscan.io], chainId=11155111,
// chainName=null, iconUrls=[https://app.dynamic.xyz/assets/networks/sepolia.svg], lcdUrl=null, name=Sepolia, nameService=null,
// nativeCurrency=NativeCurrency(decimals=18, name=Sepolia Ether, symbol=ETH), networkId=11155111, privateCustomerRpcUrls=null,
// rpcUrls=[https://gateway.tenderly.co/public/sepolia], vanityName=Sepolia)
    private fun loadSepoliaNetwork() {
        viewModelScope.launch {
            try {
                val wallet = sdk.wallets.userWallets.first { it.chain == "EVM" }
//                val networkEVM = sdk.networks.evm[1] //getting Sepolia network
                val polygonNetwork = Network(JsonPrimitive(11155111))
                sdk.wallets.switchNetwork(wallet, polygonNetwork)
                val networkResult = sdk.wallets.getNetwork(wallet)
                _network2.value = networkResult.value.toString()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load Sepolia network: ${e.message}"
            }
        }
    }

    private fun loadWallet() {
        viewModelScope.launch {
            try {
                val wallet = sdk.wallets.userWallets.firstOrNull() { it.chain == "EVM" }
                if (wallet == null) {
                    return@launch
                } else {
                    _wallet.value = wallet.address
                    _address.value = wallet.address
                    _chain.value = wallet.chain
                    loadSepoliaNetwork()
                    loadBalance()
                }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load wallet: ${e.message}"
            }
        }
    }

    private fun sendTransaction() {
        TODO("Not yet implemented")
    }

    fun logout() {
        viewModelScope.launch {
            try {
                sdk.auth.logout()
            } catch (e: Exception) {
                _errorMessage.value = "Logout failed: ${e.message}"
            }
        }
    }

    private fun navigateBack() {
        TODO("Not yet implemented")
    }

    private fun refreshData() {
        _isRefreshing.value = true
        try {
            loadWallet()
        } catch (e: Exception) {
            _errorMessage.value = "Failed to refresh data: ${e.message}"
        } finally {
            _isRefreshing.value = false
        }
    }
}
