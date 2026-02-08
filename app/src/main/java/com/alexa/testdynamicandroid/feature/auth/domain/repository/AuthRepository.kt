package com.alexa.testdynamicandroid.feature.auth.domain.repository

interface AuthRepository {
    suspend fun sendOtp(email: String): Result<Unit>
    suspend fun verifyOtp(otp: String): Result<Unit>
}
