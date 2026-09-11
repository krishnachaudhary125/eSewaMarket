package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.PaymentOptions
import com.example.eSewaMarket.data.models.ProductResponse

@Composable
fun ConfirmationCard(
    modifier: Modifier = Modifier,
    checkoutProducts: List<ProductResponse>,
    shippingAddress: String,
    paymentOption: PaymentOptions,
    totalAmount: Double,
    taxAmount: Double,
    deliveryCharge: Double,
    grandTotal: Double,
    buttonText: String,
    buttonEnabled: Boolean = true,
    onConfirmClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Payment Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_400)
                    )

                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {
                        checkoutProducts.forEach { product ->

                            ConfirmationProductDetails(
                                singleProductQuantity = product.quantity,
                                productName = product.title,
                                productTotalPrice =
                                    product.price * product.quantity
                            )
                        }
                    }

                    TextRow(
                        firstText = "Delivery Address",
                        secondText = shippingAddress
                    )

                    TextRow(
                        firstText = "Payment Option",
                        secondText = when (paymentOption) {
                            PaymentOptions.CASH_ON_DELIVERY -> "Cash on Delivery"
                            PaymentOptions.ESEWA -> "eSewa"
                        }
                    )

                    TextRow(
                        firstText = "Total Amount",
                        secondText = "Rs. ${"%.2f".format(totalAmount)}"
                    )

                    TextRow(
                        firstText = "Tax Amount",
                        secondText = "Rs. ${"%.2f".format(taxAmount)}"
                    )

                    TextRow(
                        firstText = "Delivery Charge",
                        secondText = "Rs. ${"%.2f".format(deliveryCharge)}"
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.text_dark_100),
                        modifier = Modifier
                            .padding(
                                bottom = 16.dp
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Total Paying Amount",
                            fontSize = 14.sp,
                            letterSpacing = 1.sp,
                            color = colorResource(R.color.text_dark_300),
                            modifier = Modifier
                                .width(125.dp)
                        )

                        Text(
                            "Rs. ${"%.2f".format(grandTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = 1.sp,
                            color = colorResource(R.color.text_dark_400)
                        )
                    }
                }
            }

            Button(
                onClick = onConfirmClick,
                enabled = buttonEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    buttonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}