package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.AddressResponse

@Composable
fun ShippingAddressScreen(
    shippingAddresses: List<AddressResponse>,
    onBackClick: () -> Unit,
    noOfAddress: Int,
    addAddressNow: () -> Unit
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

        if (noOfAddress > 0) {

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
            ) {

                items(
                    items = shippingAddresses,
                    key = { addresses ->
                        addresses.id
                    }
                ) { addresses ->

                    ShippingAddressCard(
                        fullName = addresses.fullName,
                        label = addresses.label.toString(),
                        addressName = "${addresses.addressName}, ${addresses.district}, ${addresses.city} ${addresses.postalCode}",
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
    }
}