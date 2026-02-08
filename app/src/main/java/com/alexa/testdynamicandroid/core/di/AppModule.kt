package com.alexa.testdynamicandroid.core.di

import android.app.ActivityManager
import android.content.Context
import com.alexa.testdynamicandroid.feature.auth.data.repository.AuthRepositoryImpl
import com.alexa.testdynamicandroid.feature.auth.domain.repository.AuthRepository
import com.alexa.testdynamicandroid.feature.transaction.data.repository.TransactionRepositoryImpl
import com.alexa.testdynamicandroid.feature.transaction.domain.repository.TransactionRepository
import com.alexa.testdynamicandroid.feature.wallet.data.repository.WalletRepositoryImpl
import com.alexa.testdynamicandroid.feature.wallet.domain.repository.WalletRepository
import com.dynamic.sdk.android.DynamicSDK
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context,
    ): Context = context

    @Provides
    @Singleton
    fun provideDynamicSDK(): DynamicSDK = DynamicSDK.getInstance()

}
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WalletModule {

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): WalletRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}