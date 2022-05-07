package dev.logickoder.kodecamp.maps.ui.shared.components

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MapType

@Composable
fun MapContainer(
    markers: List<Location>,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    DropdownField(
        modifier = Modifier.fillMaxWidth(),
        currentItem = mapType,
        items = MapType.values().toList(),
        onItemClick = { mapType = it }
    )
    Spacer(modifier = Modifier.height(24.dp))
    Map(
        markers = markers,
        modifier = Modifier.fillMaxWidth(),
        mapType = mapType,
    )
}
