package dev.logickoder.geofence.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import dev.logickoder.geofence.utils.AppState


@Composable
fun GeofenceScreen(
    locations: List<LatLng>,
    modifier: Modifier = Modifier,
    updateLocations: (List<LatLng>) -> Unit,
) {
    GoogleMap(
        modifier = modifier,
        properties = MapProperties(
            isBuildingEnabled = true,
            isTrafficEnabled = true,
        ),
        cameraPositionState = rememberCameraPositionState(
            init = {
                AppState.location?.let {
                    position = CameraPosition.fromLatLngZoom(it, 20f)
                }
            }
        ),
        content = {
            AppState.transitions.forEach { fence ->
                Circle(
                    center = locations.first { it.toString() == fence.requestId }
                )
            }
            LaunchedEffect(AppState.transitions) {
                val ids = AppState.transitions.map { it.requestId }
                val processed = locations.filterNot { it.toString() in ids }
                updateLocations(processed)
            }
        }
    )
}
