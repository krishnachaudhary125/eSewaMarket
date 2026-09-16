package com.example.eSewaMarket.ui.compose.favourite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun FavouriteEmpty(
    isLoggedIn: Boolean,
    continueShopping: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 326.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_empty_cart),
                contentDescription = "Empty Favourite",
                modifier = Modifier.padding(top = 32.dp)
            )

            Text(
                "No favourites yet",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier.padding(8.dp)
            )

            Text(
                if (isLoggedIn) "Add your favourites to wishlist and\nthey will show here." else "Login to add items in favourite.",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 1.sp,
                lineHeight = 24.sp,
                color = colorResource(id = R.color.text_dark_200),
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = continueShopping,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 32.dp)
            ) {
                Text(
                    if (isLoggedIn) "CONTINUE SHOPPING" else "LOGIN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}