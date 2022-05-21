package dev.logickoder.geofence.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng

object AppState {
    var location: LatLng? by mutableStateOf(null)
    var transitions: List<Geofence> by mutableStateOf(emptyList())
}
