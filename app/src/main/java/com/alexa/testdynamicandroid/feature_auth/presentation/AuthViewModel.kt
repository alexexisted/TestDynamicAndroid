package com.alexa.testdynamicandroid.feature_auth.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isSendOtpLoading = MutableStateFlow(false)
    val isSendOtpLoading: StateFlow<Boolean> = _isSendOtpLoading.asStateFlow()

    val isSendOtpButtonEnabled: StateFlow<Boolean> = _email.map {
        validateEmail(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isOtpBsVisible = MutableStateFlow(false)
    val isOtpBSVisible: StateFlow<Boolean> = _isOtpBsVisible.asStateFlow()

    private val _otpCode = mutableStateListOf("", "", "", "", "", "")
    val otpCode: List<String> = _otpCode

    private val _isVerifyOtpButtonEnabled = MutableStateFlow(false)
    val isVerifyOtpButtonEnabled: StateFlow<Boolean> = _isVerifyOtpButtonEnabled.asStateFlow()

    val sdk = DynamicSDK.getInstance()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnEmailChanged -> onEmailChanged(action.email)
            AuthAction.OnSendOtpClicked -> sendEmail()
            is AuthAction.OnOtpCodeChanged -> onOtpCodeChanged(action.index, action.code)
            AuthAction.OnResendCodeClicked -> sendEmail()
            AuthAction.OnVerifyCodeClicked -> verifyOtp()
        }
    }

    private fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    private fun resendEmail() {
        for (index in otpCode.indices) {
            _otpCode[index] = ""
        }
        sendEmail()
    }

    private fun sendEmail() {
        if (isSendOtpButtonEnabled.value) {
            viewModelScope.launch {
                _isSendOtpLoading.value = true
                try {
                    sdk.auth.email.sendOTP(email.value)
                    _isOtpBsVisible.value = true
                } catch (e: Exception) {
                    // Handle error appropriately
                } finally {
                    _isSendOtpLoading.value = false
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
        return Pattern.compile(emailRegex).matcher(email).matches()
    }

    private fun onOtpCodeChanged(index: Int, code: String) {
        if (index in _otpCode.indices && code.length <= 1) {
            _otpCode[index] = code
        }
        _isVerifyOtpButtonEnabled.value = validateOtp()
    }

    private fun validateOtp(): Boolean {
        return otpCode.all { it.isNotEmpty() }
    }

    private fun verifyOtp() {
        val verificationToken = otpCode.joinToString(separator = "")

        if (verificationToken.length == 6 && verificationToken.all { it.isDigit() }) {
            viewModelScope.launch {
                try {
                    sdk.auth.email.verifyOTP(verificationToken)
                } catch (
                    e: Exception
                ) { }
            }
        } else {
            // Handle invalid OTP format (e.g., show an error message)
        }

    }
}