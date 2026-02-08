package com.alexa.testdynamicandroid.feature.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexa.testdynamicandroid.core.ui.toUIError
import com.alexa.testdynamicandroid.feature.transaction.domain.repository.TransactionRepository
import com.dynamic.sdk.android.Models.BaseWallet
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _userWalletAddress = MutableStateFlow("")
    val userWalletAddress: StateFlow<String> = _userWalletAddress.asStateFlow()

    private val _userWalletBalance = MutableStateFlow("")
    val userWalletBalance: StateFlow<String> = _userWalletBalance.asStateFlow()

    private val _baseWallet = MutableStateFlow(BaseWallet("", "", ""))
    val baseWallet: StateFlow<BaseWallet> = _baseWallet.asStateFlow()

    private val _destinationAddress = MutableStateFlow("")
    val destinationAddress: StateFlow<String> = _destinationAddress.asStateFlow()

    private val _valueToSend = MutableStateFlow("")
    val valueToSend: StateFlow<String> = _valueToSend.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _isTransactionSuccessful = MutableStateFlow(false)
    val isTransactionSuccessful: StateFlow<Boolean> = _isTransactionSuccessful.asStateFlow()

    private val hash = MutableStateFlow("")
    val txHash: StateFlow<String> = hash.asStateFlow()

    private val isSendButtonEnabled = MutableStateFlow(false)
    val sendButtonEnabled: StateFlow<Boolean> = isSendButtonEnabled.asStateFlow()

    private val _uiEventCopyAddress = MutableSharedFlow<String>()
    val uiEventCopyAddress = _uiEventCopyAddress.asSharedFlow()

    init {
        loadWallet()
        loadWalletAddress()
        loadBalance()
    }

    fun onAction(action: TransactionAction) {
        when (action) {
            is TransactionAction.LoadWallet -> loadWalletAddress()
            is TransactionAction.OnDestinationAddressChanged -> onDestinationUpdated(action.newAddress)
            is TransactionAction.OnValueToSendChanged -> onValueToSendUpdated(action.newValue)
            TransactionAction.OnCopyAddressClicked -> copyAddress()
            TransactionAction.OnSendTransactionClicked -> startTransaction()
        }
    }

    private fun copyAddress() {
        viewModelScope.launch {
            _uiEventCopyAddress.emit(hash.value)
        }
    }

    private fun onValueToSendUpdated(newValue: String) {
        _valueToSend.value = newValue
        validateSendButton()
    }

    private fun onDestinationUpdated(newAddress: String) {
        _destinationAddress.value = newAddress
        validateSendButton()
    }

    private fun validateSendButton() {
        isSendButtonEnabled.value = (destinationAddress.value.isNotBlank()
                && destinationAddress.value.matches(Regex("^0x[a-fA-F0-9]{40}$"))
                && valueToSend.value.isNotBlank()
                && valueToSend.value <= userWalletBalance.value)
    }

    private fun loadWalletAddress() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.getEvmWalletAddress()
                .onSuccess { address ->
                    _userWalletAddress.value = address
                }
                .onFailure { e ->
                    _errorMessage.value = "Failed to load wallet: ${e.message.toUIError()}"
                }
        }
    }

    private fun loadWallet() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.getEvmWallet()
                .onSuccess { wallet ->
                    _baseWallet.value = wallet
                }
                .onFailure { e ->
                    _errorMessage.value = "Failed to load wallet: ${e.message.toUIError()}"
                }
        }
    }

    private fun loadBalance() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.getEvmBalance()
                .onSuccess { balance ->
                    _userWalletBalance.value = balance
                }
                .onFailure { e ->
                    _errorMessage.value = "Failed to load balance: ${e.message.toUIError()}"
                }
        }
    }

    private fun startTransaction() {
        _errorMessage.value = ""
        viewModelScope.launch {
            repository.sendEvmTransaction(
                fromAddress = userWalletAddress.value,
                destinationAddress = destinationAddress.value,
                amount = valueToSend.value,
                baseWallet = baseWallet.value
            ).onSuccess { txHash ->
                hash.value = txHash
                _isTransactionSuccessful.value = true
            }.onFailure { e ->
                _errorMessage.value = "Failed to prepare transaction: ${e.message.toUIError()}"
            }
        }
    }
}