package com.alexa.testdynamicandroid.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.BaseWallet
import com.dynamic.sdk.android.Models.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SessionViewModel @Inject constructor() : ViewModel() {
    val sdk = DynamicSDK.Companion.getInstance()
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user.asStateFlow()

    private val _wallets = MutableStateFlow<List<BaseWallet>>(emptyList())
    val wallets: StateFlow<List<BaseWallet>> = _wallets.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    private val _isCreatingWallets = MutableStateFlow(false)
    val isCreatingWallets: StateFlow<Boolean> = _isCreatingWallets.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        setupObservers()
    }


    @OptIn(FlowPreview::class)
    private fun setupObservers() {
        // Observe authentication state
        viewModelScope.launch {
            sdk.auth.authenticatedUserChanges.collect { user ->
                _isAuthenticated.value = user != null
                _user.value = user

                if (user == null) {
                    _wallets.value = emptyList()
                    _isCreatingWallets.value = false
                } else if (_wallets.value.isEmpty()) {
                    _isCreatingWallets.value = true
                }
                _isLoading.value = false
            }
        }
        // Observe wallet changes
        viewModelScope.launch {
            sdk.wallets.userWalletsChanges.collect { wallets ->
                _wallets.value = wallets
                if (wallets.isNotEmpty()) {
                    _isCreatingWallets.value = false
                }
            }
        }

        // Observe token changes
        viewModelScope.launch {
            sdk.auth.tokenChanges.collect { token ->
                _token.value = token
            }
        }
    }


}