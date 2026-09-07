package com.example.eSewaMarket.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.eSewaMarket.data.models.SelectedLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

suspend fun searchLocation(
    context: Context,
    query: String
): LatLng? {

    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context)

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(
                query,
                1
            )

            addresses?.firstOrNull()?.let {
                LatLng(
                    it.latitude,
                    it.longitude
                )
            }

        } catch (e: Exception) {
            Log.e(
                "LOCATION",
                "Unable to find location",
                e
            )
            null
        }
    }
}


suspend fun reverseGeocode(
    context: Context,
    latLng: LatLng
): SelectedLocation? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context)

            @Suppress("DEPRECATION")
            val results = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            val address = results?.firstOrNull()

            if (address != null) {
                SelectedLocation(
                    province = address.adminArea,
                    district = address.subAdminArea,
                    postalCode = address.postalCode,
                    addressName = listOfNotNull(
                        address.subLocality,
                        address.locality
                    ).distinct().joinToString(", ")
                        .ifEmpty {
                            address.getAddressLine(0) ?: "Unknown location"
                        }
                )
            } else {
                null
            }

        } catch (e: Exception) {
            Log.e("LOCATION", "Reverse geocoding failed", e)
            null
        }
    }
}


suspend fun findNearByPlaceName(
    placesClient: PlacesClient,
    latLng: LatLng,
    radiusMeter: Double = 60.0
): String? {

    return withContext(Dispatchers.IO) {
        try {
            val placeFields = listOf(
                Place.Field.DISPLAY_NAME,
                Place.Field.LOCATION
            )

            val circle = CircularBounds.newInstance(
                latLng,
                radiusMeter
            )

            val request = SearchNearbyRequest.builder(circle, placeFields)
                .setMaxResultCount(5)
                .build()

            val response = placesClient.searchNearby(request).await()

            response.places
                .mapNotNull { place ->
                    val loc = place.location ?: return@mapNotNull null
                    val distance = distanceMeters(latLng, loc)
                    place.displayName?.let { name -> name to distance }                }
                .minByOrNull { it.second }
                ?.first

        } catch (e: Exception) {
            Log.e("LOCATION", "Search nearby failed", e)
            null
        }
    }
}


suspend fun resolveLocationDetails(
    context: Context,
    placesClient: PlacesClient,
    latLng: LatLng
): SelectedLocation? {

    val nearbyPlaceName = findNearByPlaceName(
        placesClient,
        latLng,
        60.0
    )

    val location = reverseGeocode(
        context,
        latLng
    )

    return location?.copy(
        addressName = nearbyPlaceName ?: location.addressName
    )
}


private fun distanceMeters(a: LatLng, b: LatLng): Double {
    val results = FloatArray(1)
    Location.distanceBetween(
        a.latitude,
        a.longitude,
        b.latitude,
        b.longitude,
        results
    )
    return results[0].toDouble()
}