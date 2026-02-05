package com.alexa.testdynamicandroid.feature_auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexa.testdynamicandroid.R
import com.alexa.testdynamicandroid.feature_auth.presentation.components.AuthButton
import com.alexa.testdynamicandroid.feature_auth.presentation.components.AuthTextField

@Composable
fun LoginScreen(
    onAction: (AuthAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Crypto Wallet",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Please sign in to continue",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthTextField(
            value = "test",
            onValueChange = {},
            label = "Enter the email",
            placeholder = "example@gmail.com",
            modifier = Modifier,
            enabled = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthButton(
            text = "Send OTP",
            onClick = {},
            modifier = Modifier,
            enabled = false,
            isLoading = false
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen { }
}

