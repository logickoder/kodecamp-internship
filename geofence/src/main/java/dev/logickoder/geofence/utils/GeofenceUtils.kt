package dev.logickoder.geofence.utils

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

fun LatLng.createGeofence(
    radius: Float,
): Geofence {
    return Geofence.Builder()
        .setRequestId(toString())
        .setCircularRegion(
            latitude,
            longitude,
            radius,
        )
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .build()
}

fun List<LatLng>.getGeofencingRequest(
    radius: Float = 100f
): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(map { location -> location.createGeofence(radius) })
    }.build()
}
