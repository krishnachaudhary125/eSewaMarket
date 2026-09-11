package com.example.eSewaMarket.ui.compose.shippingAddress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.AddressResponse
import com.example.eSewaMarket.ui.compose.AppToolBar

@Composable
fun ShippingAddressScreen(
    shippingAddresses: List<AddressResponse>,
    onBackClick: () -> Unit,
    noOfAddress: Int,
    addAddressNow: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onAddAddressClick: () -> Unit
) {
    Scaffold(
        containerColor = colorResource(id = R.color.background),
        topBar = {
            AppToolBar(
                modifier = Modifier
                    .statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "Shipping Address",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }
            )
        }

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(
                    innerPadding
                ),
            contentAlignment = Alignment.TopStart
        ) {
            if (noOfAddress > 0) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    items(
                        items = shippingAddresses,
                        key = { addresses ->
                            addresses.id
                        }
                    ) { addresses ->

                        ShippingAddressDraggable(
                            onDeleteClick = {
                                onDeleteClick()
                            },
                            onEditClick = {
                                onEditClick()
                            },
                            fullName = addresses.fullName,
                            label = addresses.label,
                            addressName = "${addresses.addressName}, ${addresses.district} ${addresses.postalCode}",
                            province = addresses.province
                        )
                    }
                }

            } else {

                ShippingAddressEmpty(
                    modifier = Modifier
                        .padding(innerPadding),
                    addAddressNow = addAddressNow
                )
            }

            Button(
                onClick = onAddAddressClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 24.dp,
                        end = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Plus icon",
                        tint = Color.Unspecified
                    )
                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )
                    Text(
                        "ADD ADDRESS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}