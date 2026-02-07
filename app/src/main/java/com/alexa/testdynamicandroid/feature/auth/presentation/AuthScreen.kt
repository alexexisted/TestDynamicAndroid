package com.alexa.testdynamicandroid.feature.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexa.testdynamicandroid.R
import com.alexa.testdynamicandroid.feature.auth.presentation.components.AuthButton
import com.alexa.testdynamicandroid.feature.auth.presentation.components.AuthTextField
import com.alexa.testdynamicandroid.feature.auth.presentation.components.OtpScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val isSendOtpLoading by viewModel.isSendOtpLoading.collectAsState()
    val isSendOtpButtonEnabled by viewModel.isSendOtpButtonEnabled.collectAsState()
    val isOtpBsVisible by viewModel.isOtpBSVisible.collectAsState()
    val otpCode = viewModel.otpCode //because we use mutableStateListOf we can directly access it without collectAsState
    val isVerifyOtpButtonEnabled by viewModel.isVerifyOtpButtonEnabled.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
            value = email,
            onValueChange = { viewModel.onAction(AuthAction.OnEmailChanged(it)) },
            label = "Enter the email",
            placeholder = "example@gmail.com",
            modifier = Modifier,
            enabled = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthButton(
            text = "Send OTP",
            onClick = { viewModel.onAction(AuthAction.OnSendOtpClicked) },
            modifier = Modifier,
            enabled = isSendOtpButtonEnabled,
            isLoading = isSendOtpLoading
        )
    }

    if (isOtpBsVisible) {
        ModalBottomSheet(
            onDismissRequest = { /* Handle dismiss logic here */ },
            modifier = Modifier,
            sheetState = sheetState,
            sheetGesturesEnabled = true,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                OtpScreen(
                    email = email,
                    otpCode = otpCode,
                    isVerifyButtonEnabled = isVerifyOtpButtonEnabled,
                    onVerifyCodeClicked = {viewModel.onAction(AuthAction.OnVerifyCodeClicked)},
                    onResendCodeClicked = {viewModel.onAction(AuthAction.OnSendOtpClicked)},
                    onCodeChange = {index, code -> viewModel.onAction(AuthAction.OnOtpCodeChanged(index, code))},
                    onDismiss = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    AuthScreen()
}