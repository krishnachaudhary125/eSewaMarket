package com.example.eSewaMarket.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eSewaMarket.R

@Composable
fun CheckoutBottomBar(
    modifier: Modifier,
    totalPrice: Double,
    isExpanded: Boolean,
    itemCount: Int,
    productPrice: Double,
    totalTax: Double,
    shippingCharge: Double,
    onToggleClick: () -> Unit
) {

    ConstraintLayout {
        val (box, icon) = createRefs()
        Box(
            modifier = modifier
                .constrainAs(box) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(top = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(vertical = 16.dp)
            ) {
                if (isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    bottom = 4.dp
                                )
                        ) {
                            Text(
                                "Sub Total (${itemCount} items)",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 1.sp,
                                color = colorResource(id = R.color.text_dark_300)
                            )

                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Text(
                                text = "Rs. %,.2f".format(productPrice),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 2.sp,
                                color = colorResource(id = R.color.text_dark_400)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(
                                    vertical = 4.dp)
                        ) {
                            Text(
                                "Tax",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 1.sp,
                                color = colorResource(id = R.color.text_dark_300)
                            )

                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Text(
                                text = "Rs. %,.2f".format(totalTax),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 2.sp,
                                color = colorResource(id = R.color.text_dark_400)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 40.dp
                                )
                        ) {
                            Text(
                                "Shipping Charge",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 1.sp,
                                color = colorResource(id = R.color.text_dark_300)
                            )

                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Text(
                                text = "Rs. %,.2f".format(shippingCharge),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 2.sp,
                                color = colorResource(id = R.color.text_dark_400)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Grand Total",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )

                    Text(
                        "*included TAX",
                        fontSize = 10.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_200),
                        modifier = modifier
                            .padding(
                                start = 8.dp,
                                top = 4.dp
                            )
                    )
                    Spacer(
                        modifier = modifier
                            .weight(1f)
                    )

                    Text(
                        "Rs.",
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.green),
                        modifier = Modifier
                            .padding(
                                top = 4.dp
                            )
                    )

                    Text(
                        text = " %,.2f".format(totalPrice),
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.green)
                    )
                }
            }
        }

        Icon(
            painter = if (isExpanded) {
                painterResource(id = R.drawable.ic_arrow_down)
            } else {
                painterResource(id = R.drawable.ic_arrow_up)
            },
            contentDescription = "Bottom Bar Toggle",
            tint = Color.White,
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = colorResource(id = R.color.green),
                    shape = CircleShape
                )
                .padding(8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleClick
                )
                .constrainAs(icon) {
                    top.linkTo(box.top, margin = (-10).dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}
