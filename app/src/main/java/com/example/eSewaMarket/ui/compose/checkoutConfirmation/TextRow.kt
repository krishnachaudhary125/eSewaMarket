package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun TextRow(
    firstText: String,
    secondText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            firstText,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            color = colorResource(R.color.text_dark_300),
            modifier = Modifier
                .width(125.dp)
        )

        Text(
            secondText,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            letterSpacing = 1.sp,
            color = colorResource(R.color.text_dark_400)
        )
    }
}