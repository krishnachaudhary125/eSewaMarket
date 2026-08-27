package com.example.eSewaMarket.ui.compose

import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.eSewaMarket.R
import com.example.eSewaMarket.utils.searchLocation
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

@Composable
fun MapScreen(
    onLocationSelected: (LatLng) -> Unit
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(27.6790101, 85.3164678),
            15f
        )
    }

    var selectedLocation by remember {
        mutableStateOf(cameraPositionState.position.target)
    }

    var selectedAddress by remember {
        mutableStateOf("Select a location")
    }

    val context = LocalContext.current

    val addressState = rememberTextFieldState()

    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    fun performSearch() {

        val query = addressState.text.toString().trim()

        if (query.isEmpty()) return

        keyboardController?.hide()

        scope.launch {

            val location = searchLocation(
                context = context,
                query = query
            )

            location?.let {

                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        15f
                    )
                )
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedLocation = cameraPositionState.position.target
            onLocationSelected(selectedLocation)
        }
    }

    LaunchedEffect(selectedLocation) {

        val address = withContext(Dispatchers.IO) {
            try {
                val geoCoder = Geocoder(context)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    suspendCancellableCoroutine { continuation ->
                        geoCoder.getFromLocation(
                            selectedLocation.latitude,
                            selectedLocation.longitude,
                            1
                        ) { addresses ->

                            continuation.resume(
                                addresses.firstOrNull()?.getAddressLine(0)
                                    ?: ""
                            )
                        }
                    }

                } else {

                    @Suppress("DEPRECATION")
                    geoCoder.getFromLocation(
                        selectedLocation.latitude,
                        selectedLocation.longitude,
                        1
                    )?.firstOrNull()?.getAddressLine(0) ?: ""
                }

            } catch (e: Exception) {
                Log.e("LOCATION", "Unable to get location", e)
                ""
            }
        }

        selectedAddress = address

        addressState.edit {
            replace(0, length, address)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        Icon(
            painter = painterResource(R.drawable.ic_map_pointer),
            contentDescription = "Selected location",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .offset(
                    y = (-24).dp
                )
        )

        Text(
            text = selectedAddress,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
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
                        onClick = {
                            addressState.clearText()
                        }
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
                    performSearch()
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
                        onClick = {
                            performSearch()
                        }
                    )
                    .padding(16.dp)
            )
        }
    }
}
