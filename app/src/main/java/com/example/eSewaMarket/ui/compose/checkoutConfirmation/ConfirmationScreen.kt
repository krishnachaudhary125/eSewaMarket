package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.PaymentOptions
import com.example.eSewaMarket.ui.compose.AppToolBar
import com.example.eSewaMarket.ui.compose.component.CommonError
import com.example.eSewaMarket.ui.compose.component.CommonLoading

@Composable
fun ConfirmationScreen(
    state: ConfirmationUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onConfirmOrder: () -> Unit,
    onPayWithEsewa: () -> Unit
) {
    Scaffold(
        containerColor = colorResource(R.color.background),
        topBar = {
            AppToolBar(
                modifier = Modifier
                    .statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "Confirmation",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }
            )
        }
    ) { innerPadding ->

        when (state) {

            is ConfirmationUiState.Loading -> {
                CommonLoading()
            }

            is ConfirmationUiState.Success -> {

                val isEsewaPayment = state.confirmationData.paymentOption == PaymentOptions.ESEWA

                ConfirmationCard(
                    modifier = Modifier
                        .padding(innerPadding),
                    checkoutProducts = state.confirmationData.checkoutProducts,
                    shippingAddress = state.confirmationData.shippingAddress,
                    paymentOption = state.confirmationData.paymentOption,
                    taxAmount = state.confirmationData.taxAmount,
                    totalAmount = state.confirmationData.totalAmount,
                    deliveryCharge = state.confirmationData.deliveryCharge,
                    grandTotal = state.confirmationData.grandTotal,
                    buttonText = if (isEsewaPayment) {
                        "PAY NOW"
                    } else {
                        "CONFIRM"
                    },
                    onConfirmClick = {
                        if (isEsewaPayment) {
                            onPayWithEsewa()
                        } else {
                            onConfirmOrder()
                        }
                    }
                )
            }

            is ConfirmationUiState.Error -> {
                CommonError(
                    message = state.message,
                    onRetry = onRetry
                )
            }
        }
    }
}