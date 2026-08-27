package com.example.eSewaMarket.ui.compose

import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.eSewaMarket.R
import kotlinx.coroutines.Dispatchers
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

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedLocation = cameraPositionState.position.target
            onLocationSelected(selectedLocation)
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedAddress = withContext(Dispatchers.IO) {

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
                                    ?: "Address not found"
                            )
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geoCoder.getFromLocation(
                        selectedLocation.latitude,
                        selectedLocation.longitude,
                        1
                    )?.firstOrNull()?.getAddressLine(0)
                        ?: "Address not found"
                }
            } catch (e: Exception) {
                Log.e("LOCATION", "Unable to get location", e)
                "Unable to get Location"
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
        )

        Text(
            text = selectedAddress,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp)
        )
    }
}