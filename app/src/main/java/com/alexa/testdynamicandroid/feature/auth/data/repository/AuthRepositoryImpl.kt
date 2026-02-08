package com.alexa.testdynamicandroid.feature.auth.data.repository

import com.alexa.testdynamicandroid.feature.auth.domain.repository.AuthRepository
import com.dynamic.sdk.android.DynamicSDK
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sdk: DynamicSDK
) : AuthRepository {

    override suspend fun sendOtp(email: String): Result<Unit> {
        return try {
            sdk.auth.email.sendOTP(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(otp: String): Result<Unit> {
        return try {
            sdk.auth.email.verifyOTP(otp)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
