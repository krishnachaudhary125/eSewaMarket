package com.example.eSewaMarket.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
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


suspend fun reverseGeocode(context: Context, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context)

            @Suppress("DEPRECATION")
            val results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val address = results?.firstOrNull()

            if (address != null) {
                listOfNotNull(
                    address.subLocality,
                    address.locality,
                    address.adminArea
                ).distinct().joinToString(", ").ifEmpty {
                    address.getAddressLine(0) ?: "Unknown location"
                }
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            "Unknown location"
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


suspend fun resolveLocationName(
    context: Context,
    placesClient: PlacesClient,
    latLng: LatLng
): String {
    findNearByPlaceName(placesClient, latLng)?.let { return it }
    return reverseGeocode(context, latLng)
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