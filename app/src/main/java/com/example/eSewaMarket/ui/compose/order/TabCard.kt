package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun TabCard(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            tabs.forEachIndexed { index, title ->

                val isSelected = selectedTabIndex == index

                Text(
                    text = title,
                    fontSize = if (isSelected) {
                        16.sp
                    } else {
                        14.sp
                    },
                    letterSpacing = 2.sp,
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = if (isSelected) {
                        colorResource(R.color.green)
                    } else {
                        colorResource(R.color.text_dark_200)
                    },
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onTabSelected(index)
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1F)
        )

        Icon(
            painter = painterResource(R.drawable.ic_slider),
            contentDescription = "Filter",
            tint = colorResource(R.color.text_dark_300),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onFilterClick
                )
        )
    }
}