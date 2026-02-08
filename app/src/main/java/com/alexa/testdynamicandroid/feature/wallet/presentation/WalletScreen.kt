package com.alexa.testdynamicandroid.feature.wallet.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexa.testdynamicandroid.R
import com.alexa.testdynamicandroid.core.ui.ErrorMessage
import com.alexa.testdynamicandroid.feature.wallet.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel(),
    onNavigateToTransaction: () -> Unit = {}
) {

    val walletAddress by viewModel.address.collectAsState()
    val walletNetwork by viewModel.network2.collectAsState()
    val walletBalance by viewModel.balance.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val chainName by viewModel.chain.collectAsState()
    val errorMsg by viewModel.errorMessage.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        viewModel.uiEventCopyAddress.collect { addressToCopy ->
            clipboardManager.setText(AnnotatedString(addressToCopy))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallet Details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { /* No-op */ }) {
                        Icon(painterResource(id = R.drawable.arrow_back_icon), contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = isRefreshing,

            onRefresh = { viewModel.onAction(WalletAction.OnRefreshDataPulled) },
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Main Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        //tag Badge
                        TagComponent(
                            name = chainName,
                            modifier = Modifier
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        //address
                        WalletAddressComponent(address = walletAddress)

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        //network
                        WalletNetworkComponent(networkName = walletNetwork.toString())

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        // Balance Section
                        WalletBalanceComponent(balance = walletBalance)
                    }
                }

                //copy
                ThemeActionButton(
                    text = "Copy Address",
                    iconId = R.drawable.copy_icon,
                    onClick = { viewModel.onAction(WalletAction.OnCopyAddressClicked) },
                    showArrow = true
                )

                //transaction
                SendTransactionButton(onClick = onNavigateToTransaction)

                //logout
                ThemeActionButton(
                    text = "Logout",
                    iconId = R.drawable.logout_icon,
                    textColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = { viewModel.onAction(WalletAction.OnLogoutClicked) },
                    showArrow = true
                )

                if (errorMsg.isNotBlank()) {
                    ErrorMessage(
                        errorMessage = errorMsg,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WalletPrev() {
    WalletScreen()
}