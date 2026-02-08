package com.alexa.testdynamicandroid.feature.auth.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexa.testdynamicandroid.core.ui.toUIError
import com.alexa.testdynamicandroid.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

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

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnEmailChanged -> onEmailChanged(action.email)
            AuthAction.OnSendOtpClicked -> sendEmail()
            is AuthAction.OnOtpCodeChanged -> onOtpCodeChanged(index = action.index, code = action.code)
            AuthAction.OnResendCodeClicked -> resendEmail()
            AuthAction.OnVerifyCodeClicked -> verifyOtp()
            AuthAction.OnDismissOtpBS -> changeBSVisibility()
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

    private fun changeBSVisibility() {
        _isOtpBsVisible.value = !_isOtpBsVisible.value
    }

    private fun sendEmail() {
        _errorMessage.value = ""
        if (!isSendOtpButtonEnabled.value) return

        viewModelScope.launch {
            _isSendOtpLoading.value = true
            val result = authRepository.sendOtp(email.value)

            result.onSuccess {
                _isOtpBsVisible.value = true
            }.onFailure { exception ->
                _errorMessage.value = (exception.message ?: "").toUIError()
            }

            _isSendOtpLoading.value = false
        }
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = """^([a-zA-Z0-9._\-+]+)@([a-zA-Z0-9._\-]+)\.([a-zA-Z]{2,})$""".toRegex()
        return emailRegex.matches(email)
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
        _errorMessage.value = ""
        val verificationToken = otpCode.joinToString(separator = "")

        if (verificationToken.length == 6 && verificationToken.all { it.isDigit() }) {
            viewModelScope.launch {
                val result = authRepository.verifyOtp(verificationToken)
                result
                    .onFailure { exception ->
                        _errorMessage.value = (exception.message ?: "").toUIError()
                    }
            }
        } else {
            _errorMessage.value = "Invalid OTP code. Please enter the 6-digit code sent to your email."
        }

    }
}