package com.example.eSewaMarket.ui.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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

@Composable
fun MapScreen(
    onLocationSelected: (LatLng) -> Unit,
    latitude: Double,
    longitude: Double,
    onSelectClick: () -> Unit
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

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedLocation = cameraPositionState.position.target
            onLocationSelected(selectedLocation)
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

            delay(300)

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

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        cameraPositionState.position = CameraPosition.Builder(
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

        Button(
            onClick = onSelectClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.green),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    y = (-56).dp
                )
        ) {
            Text(
                "Select",
                letterSpacing = 2.sp,
                fontSize = 14.sp
            )
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
                    performLocationSearch()
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
                            performLocationSearch()
                        }
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
                                selectPlace(prediction)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
