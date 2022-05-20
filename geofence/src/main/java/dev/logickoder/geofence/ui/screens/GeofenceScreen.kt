package dev.logickoder.geofence.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng


@Composable
fun GeofenceScreen(
    locations: List<LatLng>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        content = {
            locations.forEach {
                Text(it.toString())
            }
        }
    )
}