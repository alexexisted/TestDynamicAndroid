package com.alexa.testdynamicandroid.feature.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexa.testdynamicandroid.R
import com.alexa.testdynamicandroid.core.ui.ErrorMessage
import com.alexa.testdynamicandroid.feature.transaction.components.TransactionSuccessCard
import com.alexa.testdynamicandroid.feature.wallet.presentation.WalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel: TransactionViewModel = hiltViewModel()

    val isSendButtonEnabled by viewModel.sendButtonEnabled.collectAsState()
    val isTransactionSuccessful by viewModel.isTransactionSuccessful.collectAsState()
    val hash by viewModel.txHash.collectAsState()
    val destinationAddress by viewModel.destinationAddress.collectAsState()
    val valueToSend by viewModel.valueToSend.collectAsState()
    val errorMsg by viewModel.errorMessage.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        viewModel.uiEventCopyAddress.collect { addressToCopy ->
            clipboardManager.setText(AnnotatedString(addressToCopy))
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Wallet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Send Transaction",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            //destination address field
            OutlinedTextField(
                value = destinationAddress,
                onValueChange = {address -> viewModel.onAction(TransactionAction.OnDestinationAddressChanged(address))},
                label = { Text("Recipient Address") },
                placeholder = { Text("0x...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //amount to send
            OutlinedTextField(
                value = valueToSend,
                onValueChange = {value -> viewModel.onAction(TransactionAction.OnValueToSendChanged(value))},
                label = { Text("Amount (ETH)") },
                placeholder = { Text("0.0") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {viewModel.onAction(TransactionAction.OnSendTransactionClicked)},
                enabled = isSendButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Sepolia ETH", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isTransactionSuccessful) {
                TransactionSuccessCard(
                    hash = hash,
                    modifier = Modifier,
                    onCopyClick = {viewModel.onAction(TransactionAction.OnCopyAddressClicked)},
                    onViewOnEtherscanClick = {}
                )
            }
            if (errorMsg.isNotBlank()) {
                ErrorMessage(
                    errorMessage = errorMsg,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun TransPrev() {
    TransactionScreen()
}