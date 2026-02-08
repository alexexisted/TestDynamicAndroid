package com.alexa.testdynamicandroid.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alexa.testdynamicandroid.BuildConfig
import com.alexa.testdynamicandroid.core.navigation.MainNavGraph
import com.alexa.testdynamicandroid.ui.theme.TestDynamicAndroidTheme
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Dynamic SDK
        val props = ClientProps(
            environmentId = BuildConfig.ENV_ID,
            appLogoUrl = "https://demo.dynamic.xyz/favicon-32x32.png",
            appName = "Crypto Wallet Test",
            redirectUrl = "dynamictest://",
            appOrigin = "https://test.app",
            logLevel = LoggerLevel.DEBUG,
        )
        DynamicSDK.initialize(props, applicationContext, this)

        setContent {
            TestDynamicAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainNavGraph()
                        DynamicUI()
                    }
                }
            }
        }
    }
}

