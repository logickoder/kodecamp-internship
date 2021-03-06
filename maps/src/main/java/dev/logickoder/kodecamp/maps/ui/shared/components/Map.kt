package dev.logickoder.kodecamp.maps.ui.shared.components

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun Map(
    mapType: MapType,
    markers: List<Location>,
    modifier: Modifier = Modifier
) = GoogleMap(
    modifier = modifier,
    properties = MapProperties(
        mapType = mapType,
        isBuildingEnabled = true,
        isTrafficEnabled = true,
    ),
    cameraPositionState = rememberCameraPositionState {
        val pos = markers.firstOrNull() ?: return@rememberCameraPositionState
        position = CameraPosition.fromLatLngZoom(LatLng(pos.latitude, pos.longitude), 11f)
    },
    content = {
        markers.forEach { marker ->
            Marker(
                state = rememberMarkerState(position = LatLng(marker.latitude, marker.longitude))
            )
        }
    }
)
