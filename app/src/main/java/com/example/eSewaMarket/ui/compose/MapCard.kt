package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapCard(
    cameraPositionState: CameraPositionState,
    selectedAddress: String,
    latLng: String,
    addressState: TextFieldState,
    predictions: List<AutocompletePrediction>,
    onConfirmClick: () -> Unit,
    clearClick: () -> Unit,
    onKeyboardAction: () -> Unit,
    onSearchClick: () -> Unit,
    onPredictionClick: (AutocompletePrediction) -> Unit
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false
            )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            cameraPositionState.position =
                                CameraPosition.Builder(
                                    cameraPositionState.position
                                )
                                    .bearing(0f)
                                    .build()
                        }
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_gps_btn),
                    contentDescription = "Compass",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    )
            ) {

                Text(
                    text = "Selected Location",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = selectedAddress,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = latLng,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Button(
                    onClick = onConfirmClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp
                        )
                ) {
                    Text(
                        "Confirm Location",
                        letterSpacing = 2.sp,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_map_pointer),
            contentDescription = "Selected location",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp)
                .offset(
                    y = (-16).dp
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Remove address",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = clearClick
                    )
                    .padding(16.dp)
            )

            OutlinedTextField(
                contentPadding = PaddingValues(
                    horizontal = 0.dp,
                    vertical = 0.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.White
                    ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 1.sp,
                    color = colorResource(R.color.text_dark_300)
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),

                onKeyboardAction = {
                    onKeyboardAction()
                },
                state = addressState,
                placeholder = {
                    Text(
                        text = "Choose a shipping address",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_100)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Location",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSearchClick
                    )
                    .padding(16.dp)
            )
        }

        if (predictions.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 80.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                predictions.forEach { prediction ->

                    Text(
                        text = prediction.getFullText(null).toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onPredictionClick(prediction)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}