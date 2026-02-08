package com.alexa.testdynamicandroid.feature.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexa.testdynamicandroid.core.ui.toUIError
import com.alexa.testdynamicandroid.feature.wallet.domain.repository.WalletRepository
import com.dynamic.sdk.android.Models.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _network = MutableStateFlow<Network?>(null)
    val network: StateFlow<Network?> = _network.asStateFlow()

    private val _network2 = MutableStateFlow<String?>(null)
    val network2: StateFlow<String?> = _network2.asStateFlow()

    private val _balance = MutableStateFlow("")
    val balance: StateFlow<String> = _balance.asStateFlow()

    private val _wallet = MutableStateFlow("")
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
            WalletAction.OnLogoutClicked -> logout()
            WalletAction.OnRefreshDataPulled -> refreshData()
        }
    }

    private fun copyAddress() {
        viewModelScope.launch {
            _uiEventCopyAddress.emit(address.value)
        }
    }

    private fun loadBalance() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.getEvmBalance()
                .onSuccess { balance ->
                    _balance.value = balance
                }
                .onFailure { e ->
                    _errorMessage.value = e.message.toUIError()
                }
        }
    }

    private fun loadSepoliaNetwork() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.switchAndGetEvmNetwork(11155111)
                .onSuccess { network ->
                    _network2.value = network
                }
                .onFailure { e ->
                    _errorMessage.value = e.message.toUIError()
                }
        }
    }

    private fun loadWallet() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.getEvmWalletInfo()
                .onSuccess { info ->
                    if (info == null) return@onSuccess
                    _wallet.value = info.address
                    _address.value = info.address
                    _chain.value = info.chain
                    loadSepoliaNetwork()
                    loadBalance()
                }
                .onFailure { e ->
                    _errorMessage.value = e.message.toUIError()
                }
        }
    }


    fun logout() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.logout().onFailure { e ->
                _errorMessage.value = e.message.toUIError()
            }
        }
    }

    private fun refreshData() {
        _errorMessage.value = ""
        _isRefreshing.value = true
        try {
            loadWallet()
        } catch (e: Exception) {
            _errorMessage.value = e.message.toUIError()
        } finally {
            _isRefreshing.value = false
        }
    }
}
