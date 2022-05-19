package dev.logickoder.geofence.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import dev.logickoder.geofence.R
import dev.logickoder.geofence.ui.theme.Padding

const val MaxLocations = 3

@Composable
fun SelectLocationsScreen(
    modifier: Modifier = Modifier,
    selectedLocations: Int,
    onLocationSelected: (LatLng) -> Unit,
    clearLocations: () -> Unit,
    onNext: () -> Unit,
) {
    Column(
        modifier = modifier,
        content = {
            Text(
                text = stringResource(id = R.string.location_select_info)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding),
                horizontalArrangement = Arrangement.Center,
                content = {
                    Text(stringResource(R.string.location_selected_info, selectedLocations))
                    Spacer(modifier = Modifier.width(Padding))
                    Button(
                        onClick = clearLocations,
                        content = {
                            Text(stringResource(id = R.string.clear_locations))
                        }
                    )
                }
            )
            GoogleMap(
                properties = MapProperties(
                    isBuildingEnabled = true,
                    isTrafficEnabled = true,
                ),
                cameraPositionState = rememberCameraPositionState(),
                onMapLongClick = { location ->
                    if (selectedLocations < MaxLocations)
                        onLocationSelected(location)
                }
            )
            Spacer(modifier = Modifier.height(Padding))
            Button(
                onClick = onNext,
                enabled = selectedLocations == MaxLocations,
                content = {
                    Text(text = stringResource(id = R.string.next))
                }
            )
        }
    )
}
