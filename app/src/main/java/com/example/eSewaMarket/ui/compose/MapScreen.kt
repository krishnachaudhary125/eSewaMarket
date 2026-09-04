package com.example.eSewaMarket.ui.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.eSewaMarket.data.models.SelectedLocation
import com.example.eSewaMarket.utils.resolveLocationDetails
import com.example.eSewaMarket.utils.searchLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
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
    onConfirmClick: (SelectedLocation) -> Unit
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

    var selectedLocationData by remember {
        mutableStateOf<SelectedLocation?>(null)
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

    var skipNextReverseGeocode by remember {
        mutableStateOf(false)
    }

    fun selectPlace(prediction: AutocompletePrediction) {

        isSelectingPlace = true

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
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

                    skipNextReverseGeocode = true

                    predictions = emptyList()

                    keyboardController?.hide()

                    val components = place.addressComponents?.asList()

                    val province = components
                        ?.firstOrNull {
                            it.types.contains("administrative_area_level_1")
                        }
                        ?.name

                    val district = components
                        ?.firstOrNull {
                            it.types.contains("administrative_area_level_2")
                        }
                        ?.name

                    val city = components
                        ?.firstOrNull {
                            it.types.contains("sublocality_level_1")
                        }
                        ?.name

                    val postalCode = components
                        ?.firstOrNull {
                            it.types.contains("postal_code")
                        }
                        ?.name

                    val sublocality = components
                        ?.firstOrNull {
                            it.types.any { type ->
                                type == "sublocality" ||
                                        type == "sublocality_level_1"
                            }
                        }
                        ?.name

                    val addressName = place.displayName ?: "Unknown location"

                    selectedLocationData = SelectedLocation(
                        province = province,
                        district = district,
                        city = city,
                        postalCode = postalCode,
                        addressName = addressName
                    )

                    addressState.edit {
                        replace(
                            0,
                            length,
                            addressName
                        )
                    }

                    selectedAddress = addressName

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

            if (skipNextReverseGeocode) {
                skipNextReverseGeocode = false
                return@LaunchedEffect
            }

            isReverseGeocoding = true

            val locationData = resolveLocationDetails(
                context,
                placesClient,
                selectedLocation
            )

            selectedLocationData = locationData

            selectedAddress =
                locationData?.addressName ?: "Unknown location"

            isReverseGeocoding = false
        }
    }

    LaunchedEffect(Unit) {

        snapshotFlow {
            addressState.text.toString()
        }.collectLatest { text ->

            if (isSelectingPlace) {
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
        onConfirmClick = {
            selectedLocationData?.let {
                onConfirmClick(it)
            }
        },
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
