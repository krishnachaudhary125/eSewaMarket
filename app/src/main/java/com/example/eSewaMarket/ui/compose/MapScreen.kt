package com.example.eSewaMarket.ui.compose

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.example.eSewaMarket.utils.resolveLocationName
import com.example.eSewaMarket.utils.searchLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("DefaultLocale")
@Composable
fun MapScreen(
    onLocationSelected: (LatLng) -> Unit,
    latitude: Double,
    longitude: Double,
    onConfirmClick: () -> Unit
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude),
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

    val placesClient = remember {
        Places.createClient(context)
    }

    var predictions by remember {
        mutableStateOf<List<AutocompletePrediction>>(emptyList())
    }

    val sessionToken = remember {
        AutocompleteSessionToken.newInstance()
    }

    var isSelectingPlace by remember {
        mutableStateOf(false)
    }

    fun selectPlace(prediction: AutocompletePrediction) {

        isSelectingPlace = true

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LOCATION
        )

        val request = FetchPlaceRequest.builder(
            prediction.placeId,
            placeFields
        )
            .setSessionToken(sessionToken)
            .build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->

                val place = response.place

                place.location?.let { latLng ->

                    predictions = emptyList()

                    keyboardController?.hide()

                    val components = place.addressComponents?.asList()

                    val sublocality = components
                        ?.firstOrNull { component ->
                            component.types.any {
                                it == "sublocality" || it == "sublocality_level_1"
                            }
                        }
                        ?.name

                    val locality = components
                        ?.firstOrNull { component ->
                            component.types.contains("locality")
                        }
                        ?.name

                    val shortAddress = listOfNotNull(
                        sublocality,
                        locality
                    ).distinct()
                        .joinToString(", ")

                    val addressName = buildString {
                        place.displayName?.let {
                            append(it)
                        }

                        if (shortAddress.isNotEmpty()) {
                            append(", ")
                            append(shortAddress)
                        }
                    }

                    val formattedAddress = place.formattedAddress

                    addressState.edit {
                        replace(
                            0,
                            length,
                            addressName
                        )
                    }

                    selectedAddress = formattedAddress ?: addressName

                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                17f
                            )
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->

                Log.e("PLACES", "Failed to fetch place", exception)
            }
    }

    var isReverseGeocoding by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedLocation = cameraPositionState.position.target
            onLocationSelected(selectedLocation)

            isReverseGeocoding = true
            selectedAddress = resolveLocationName(context, placesClient, selectedLocation)
            isReverseGeocoding = false
        }
    }

    LaunchedEffect(Unit) {

        snapshotFlow {
            addressState.text.toString()
        }.collectLatest { text ->

            if (isSelectingPlace) {
                isSelectingPlace = false
                predictions = emptyList()
                return@collectLatest
            }

            val query = text.trim()

            if (query.length < 3) {
                predictions = emptyList()
                return@collectLatest
            }

            delay(300.milliseconds)

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("NP")
                .setSessionToken(sessionToken)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->

                    Log.d(
                        "PLACES",
                        "Predictions count = ${response.autocompletePredictions.size}"
                    )

                    response.autocompletePredictions.forEach {
                        Log.d(
                            "PLACES",
                            "Prediction = ${it.getFullText(null)}"
                        )
                    }

                    predictions = response.autocompletePredictions
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "PLACES",
                        "Autocomplete failed",
                        exception
                    )

                    predictions = emptyList()
                }
        }
    }

    fun performLocationSearch() {
        val query = addressState.text.toString().trim()

        if (query.length <= 3) {
            return
        }

        keyboardController?.hide()

        scope.launch {
            val latLng = searchLocation(
                context = context,
                query = query
            )

            latLng?.let {
                predictions = emptyList()

                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        17f
                    )
                )
            }
        }
    }

    MapCard(
        cameraPositionState = cameraPositionState,
        selectedAddress = when {
            cameraPositionState.isMoving -> "Selecting location..."
            isReverseGeocoding -> "Loading address..."
            else -> selectedAddress
        },
        latLng = String.format(
            "%.6f, %.6f",
            selectedLocation.latitude,
            selectedLocation.longitude
        ),
        addressState = addressState,
        predictions = predictions,
        onConfirmClick = onConfirmClick,
        clearClick = {
            addressState.clearText()
        },
        onKeyboardAction = {
            performLocationSearch()
        },
        onSearchClick = {
            performLocationSearch()
        },
        onPredictionClick = { prediction ->
            selectPlace(prediction)
        }
    )
}
