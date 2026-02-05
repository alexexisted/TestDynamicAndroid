package com.alexa.testdynamicandroid.feature_auth.presentation

sealed interface AuthAction {
    data class OnEmailChanged(val email: String) : AuthAction
    data object OnSendOtpClicked : AuthAction

    data class OnOtpCodeChanged(val index: Int, val code: String) : AuthAction
    data object OnVerifyCodeClicked : AuthAction
    data object OnResendCodeClicked : AuthAction
}