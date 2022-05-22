package dev.logickoder.geofence.utils

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

const val GeofenceRadius = 100f
const val ZoomLevel = 10f

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
        .setTransitionTypes(
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL
        )
        .setLoiteringDelay(1000)
        .build()
}

fun List<LatLng>.getGeofencingRequest(
    radius: Float = GeofenceRadius
): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(map { location -> location.createGeofence(radius) })
    }.build()
}