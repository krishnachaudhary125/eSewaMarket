package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private enum class ShippingAddressSwipeValue {
    Closed,
    Open
}

@Composable
fun ShippingAddressDraggable(
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    fullName: String,
    label: String,
    addressName: String,
    province: String
) {

    val deleteWidth = 96.dp
    val density = LocalDensity.current
    val actionsWidth = deleteWidth * 2
    val actionsWidthPx = with(density) { actionsWidth.toPx() }
    val scope = rememberCoroutineScope()

    val state = remember {
        AnchoredDraggableState(
            initialValue = ShippingAddressSwipeValue.Closed
        )
    }

    val anchors = remember(deleteWidth) {
        DraggableAnchors {
            ShippingAddressSwipeValue.Closed at 0f
            ShippingAddressSwipeValue.Open at -actionsWidthPx
        }
    }

    state.updateAnchors(anchors)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        CompositionLocalProvider(
            LocalRippleConfiguration provides null
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp
                    )
                    .background(
                        color = colorResource(id = R.color.light_300),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(
                    onClick = {
                        onDeleteClick()

                        scope.launch {
                            state.animateTo(
                                ShippingAddressSwipeValue.Closed
                            )
                        }
                    },
                    modifier = Modifier
                        .size(deleteWidth)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit shipping address",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.green),
                                shape = CircleShape
                            )
                            .padding(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        onEditClick()

                        scope.launch {
                            state.animateTo(
                                ShippingAddressSwipeValue.Closed
                            )
                        }
                    },
                    modifier = Modifier.size(deleteWidth)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = "Delete shipping address",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.delete_red),
                                shape = CircleShape
                            )
                            .padding(16.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(
                            x = if (state.offset.isNaN()) {
                                0
                            } else {
                                state.offset.roundToInt()
                            },
                            y = 0
                        )
                    }
                    .anchoredDraggable(
                        state = state,
                        orientation = Orientation.Horizontal
                    )
            ) {
                ShippingAddressCard(
                    fullName = fullName,
                    label = label,
                    addressName = addressName,
                    province = province
                )
            }
        }
    }
}