package com.example.eSewaMarket.ui.compose.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.ui.compose.AppToolBar
import com.example.eSewaMarket.ui.compose.component.CommonError
import com.example.eSewaMarket.ui.compose.component.CommonLoading
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    uiState: CheckoutUiState,
    onBackClick: () -> Unit,
    onProductClick: (ProductResponse) -> Unit,
    onSetAddressClick: () -> Unit,
    chooseAddress: () -> Unit,
    cashOnDelivery: (CheckoutData) -> Unit,
    payWithEsewa: (CheckoutData) -> Unit,
    onRetry: () -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showPromoSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var showPromoSheetAddress by rememberSaveable {
        mutableStateOf(false)
    }

    val promoSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = colorResource(id = R.color.background),
        topBar = {
            AppToolBar(
                modifier = Modifier.statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "Checkout",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }
            )
        },
        bottomBar = {
            if (uiState is CheckoutUiState.Success) {
                CheckoutBottomBar(
                    modifier = Modifier.navigationBarsPadding(),
                    totalPrice = uiState.checkoutData.totalAmount,
                    isExpanded = isExpanded,
                    itemCount = uiState.checkoutData.itemCount,
                    productPrice = uiState.checkoutData.productPrice,
                    totalTax = uiState.checkoutData.totalTax,
                    shippingCharge = uiState.checkoutData.shippingCharge,
                    onToggleClick = {
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    ) { innerPadding ->

        when (uiState) {

            CheckoutUiState.Loading -> {
                CommonLoading()
            }

            is CheckoutUiState.Success -> {
                val data = uiState.checkoutData

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    item {
                        AddressCard(
                            address = {
                                Text(
                                    data.shippingAddress,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.color_charcoal),
                                    lineHeight = 24.sp,
                                    letterSpacing = 2.sp
                                )
                            },
                            onAddMapClick = {
                                if (data.addressExist) {
                                    chooseAddress()
                                } else {
                                    showPromoSheetAddress = true
                                }
                            },
                            addressExist = data.addressExist
                        )

                        Text(
                            "Order Summary",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = colorResource(id = R.color.text_dark_300),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 268.dp)
                        ) {
                            items(
                                items = data.checkoutProducts,
                                key = { product ->
                                    product.productId
                                }
                            ) { product ->
                                CheckoutProductCard(
                                    onClick = {
                                        onProductClick(product)
                                    },
                                    image = {
                                        AsyncImage(
                                            model = product.thumbnail,
                                            contentDescription = "Product Image",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    color = colorResource(id = R.color.image_bg_color)
                                                ),
                                            contentScale = ContentScale.Crop
                                        )
                                    },
                                    title = product.title,
                                    brand = product.brand,
                                    price = product.price,
                                    quantity = product.quantity
                                )
                            }
                        }

                        Button(
                            onClick = {
                                showPromoSheet = true
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = colorResource(id = R.color.green)
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = colorResource(id = R.color.green)
                            ),
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    bottom = 16.dp
                                )
                        ) {
                            Text(
                                "HAVE A PROMOCODE?",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                letterSpacing = 4.sp,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }

                        Text(
                            "Choose Your Payment Option",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 1.sp,
                            color = colorResource(id = R.color.text_dark_300),
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(),
                                        onClick = {
                                            cashOnDelivery(data)
                                        }
                                    )
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cash_on_delivery),
                                    contentDescription = "Cash on Delivery",
                                    modifier = Modifier.padding(end = 10.dp)
                                )

                                Text(
                                    "Cash on Delivery",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp,
                                    letterSpacing = 1.sp,
                                    color = colorResource(id = R.color.text_dark_300),
                                    modifier = Modifier.padding(start = 10.dp)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Cash on Delivery",
                                    modifier = Modifier
                                        .rotate(180f)
                                        .padding(horizontal = 8.dp)
                                )
                            }

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = colorResource(id = R.color.text_dark_100),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(),
                                        onClick = {
                                            payWithEsewa(data)
                                        }
                                    )
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_esewa_logo_gray),
                                    contentDescription = "Cash on Delivery",
                                    modifier = Modifier.padding(end = 10.dp)
                                )

                                Text(
                                    "Pay with eSewa",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp,
                                    letterSpacing = 1.sp,
                                    color = colorResource(id = R.color.text_dark_300),
                                    modifier = Modifier.padding(start = 10.dp)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Pay with eSewa",
                                    modifier = Modifier
                                        .rotate(180f)
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_secure),
                                contentDescription = "Secured",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                "SAFE AND SECURE PAYMENTS.\n100% AUTHENTIC PRODUCTS.",
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 5.sp,
                                color = colorResource(id = R.color.text_dark_200),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            is CheckoutUiState.Error -> {
                CommonError(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }

    if (showPromoSheetAddress) {
        BottomSheetSetAddress(
            onDismiss = {
                showPromoSheetAddress = false
            },
            sheetState = promoSheetState,
            onSetAddressClick = {
                onSetAddressClick()
                showPromoSheetAddress = false
            },
            onCancelClick = {
                scope.launch {
                    promoSheetState.hide()
                    showPromoSheetAddress = false
                }
            }
        )
    }

    if (showPromoSheet) {
        BottomSheetPromoCode(
            onDismiss = {
                showPromoSheet = false
            },
            sheetState = promoSheetState,
            onCancelClick = {
                scope.launch {
                    promoSheetState.hide()
                    showPromoSheet = false
                }
            },
            onApplyClick = {}
        )
    }
}