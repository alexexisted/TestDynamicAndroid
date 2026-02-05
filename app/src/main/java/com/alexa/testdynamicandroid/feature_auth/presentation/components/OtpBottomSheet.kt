package com.alexa.testdynamicandroid.feature_auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OtpBottomSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Icon Header
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_email), 
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
            text = "mail@gmail.com",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //mock data
            listOf("1", "2", "3", "4", "4").forEachIndexed { index, digit ->
                OtpDigitBox(
                    digit = digit,
                    onValueChange = { TODO("onAction(AuthAction.OnOtpCodeChanged(index, it))") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        AuthButton(
            text = "Verify Code",
            onClick = { TODO("onAction(AuthAction.OnVerifyCodeClicked)") },
            enabled = false,
            textStyle = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Didn't receive the code?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TextButton(onClick = { TODO("onAction(AuthAction.OnResendCodeClicked)") }) {
            Text(
                text = "Resend Code",
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun OtpPr() {
    OtpBottomSheet()
}