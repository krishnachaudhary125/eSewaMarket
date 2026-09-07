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
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.ui.compose.AppToolBar

@Composable
fun ConfirmationScreen(
    checkoutProducts: List<ProductResponse>,
    onBackClick: () -> Unit,
    shippingAddress: String,
    paymentOption: String
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
    ) { innerPadding->

        ConfirmationCard(
            modifier = Modifier
                .padding(innerPadding),
            checkoutProducts = checkoutProducts,
            shippingAddress = shippingAddress,
            paymentOption = paymentOption
        )
    }
}