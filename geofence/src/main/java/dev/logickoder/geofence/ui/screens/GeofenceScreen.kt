package dev.logickoder.geofence.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dev.logickoder.geofence.R
import dev.logickoder.geofence.ui.theme.Padding
import dev.logickoder.geofence.utils.AppState.location
import dev.logickoder.geofence.utils.AppState.transitions
import dev.logickoder.geofence.utils.GeofenceRadius
import dev.logickoder.geofence.utils.ZoomLevel
import kotlinx.coroutines.launch


@Composable
fun GeofenceScreen(
    locations: List<LatLng>,
    modifier: Modifier = Modifier,
    processedLocations: (List<LatLng>) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
                content = {
                    val myMarkerState = rememberMarkerState()
                    val myPositionState = rememberCameraPositionState()
                    location?.let {
                        myMarkerState.position = it
                        myPositionState.position = CameraPosition.fromLatLngZoom(it, ZoomLevel)
                    }
                    LaunchedEffect(transitions) {
                        val ids = transitions.map { it.requestId }
                        val processed = locations.filter { it.toString() in ids }
                        scope.launch {
                            locations.firstOrNull { it !in processed }?.let {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    context.getString(R.string.next_location, it.toString())
                                )
                            } ?: run {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    context.getString(R.string.congratulations)
                                )
                            }
                        }
                        processedLocations(processed)
                    }

                    Text(
                        text = "${locations.size} location(s) left",
                        modifier = Modifier.padding(vertical = Padding / 4)
                    )
                    GoogleMap(
                        properties = MapProperties(
                            isBuildingEnabled = true,
                            isTrafficEnabled = true,
                        ),
                        cameraPositionState = myPositionState,
                        content = {
                            Marker(state = myMarkerState)
                            locations.forEach { location ->
                                Circle(
                                    center = location,
                                    radius = GeofenceRadius.toDouble(),
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}
