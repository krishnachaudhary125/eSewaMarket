package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

private enum class FavouriteSwipeValue{
    Closed,
    Open
}

@Composable
fun FavouriteProductCard(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    brand: @Composable () -> Unit,
    price: @Composable () -> Unit,
    onClick: () -> Unit,
    optionClick: () -> Unit,
    addToCartClick: () -> Unit,
    tickClick: () -> Unit,
    checked: Boolean,
    onDeleteClick: () -> Unit
) {
    val deleteWidth = 120.dp
    val density = LocalDensity.current
    val deleteWidthPx = with(density) { deleteWidth.toPx() }
    val scope = rememberCoroutineScope()

    val state = remember {
        AnchoredDraggableState(
            initialValue = FavouriteSwipeValue.Closed
        )
    }

    val anchors = remember(deleteWidth) {
        DraggableAnchors {
            FavouriteSwipeValue.Closed at 0f
            FavouriteSwipeValue.Open at -deleteWidthPx
        }
    }

    state.updateAnchors(anchors)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.light_300),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.CenterEnd
        ){
            CompositionLocalProvider(
                LocalRippleConfiguration provides null
            ) {
                IconButton(
                    onClick = {
                        onDeleteClick()

                        scope.launch {
                            state.animateTo(
                                FavouriteSwipeValue.Closed
                            )
                        }
                    },
                    modifier = Modifier.size(deleteWidth)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = "Delete product from favourite",
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
            FavouriteProductCardContent(
                image = image,
                title = title,
                brand = brand,
                price = price,
                onClick = onClick,
                optionClick = optionClick,
                addToCartClick = addToCartClick,
                tickClick = tickClick,
                checked = checked
            )
        }
    }
}