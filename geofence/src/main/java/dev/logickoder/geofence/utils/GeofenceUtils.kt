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
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setNotificationResponsiveness(1000)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .build()
}

fun List<LatLng>.getGeofencingRequest(
    radius: Float = 1000f
): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(map { location -> location.createGeofence(radius) })
    }.build()
}
