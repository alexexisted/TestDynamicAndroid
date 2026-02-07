package com.alexa.testdynamicandroid.feature_auth.presentation.components

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexa.testdynamicandroid.feature_auth.presentation.AuthAction

@Composable
fun OtpScreen(
    email: String,
    otpCode: List<String>,
    isVerifyButtonEnabled: Boolean,
    onVerifyCodeClicked: () -> Unit,
    onResendCodeClicked: () -> Unit,
    onCodeChange: (index: Int, code: String) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequesters = remember { List(otpCode.size) { FocusRequester() } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dialog_email),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Check your email",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "We sent a verification code to",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otpCode.forEachIndexed { index, digit ->
                OtpDigitBox(
                    digit = digit,
                    onCodeChange = { newDigit ->
                        onCodeChange(index, newDigit)
                        if (newDigit.isNotEmpty() && index < otpCode.size - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    onBackspace = {
                        if (index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequesters[index])
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(
            text = "Verify Code",
            onClick = { onVerifyCodeClicked() },
            enabled = isVerifyButtonEnabled,
            textStyle = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Didn't receive the code?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TextButton(onClick = { onResendCodeClicked() }) {
            Text(
                text = "Resend Code",
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun OtpPr() {
    OtpScreen(
        email = "test",
        otpCode = mutableListOf("", "", "","","", ""),
        isVerifyButtonEnabled = false,
        onCodeChange = { _, _ -> },
        onVerifyCodeClicked = {},
        onResendCodeClicked = {},
        onDismiss = {}
    )
}