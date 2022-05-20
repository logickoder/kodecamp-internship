package dev.logickoder.geofence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.LatLng
import dev.logickoder.geofence.ui.screens.GeofenceScreen
import dev.logickoder.geofence.ui.screens.SelectLocationsScreen
import dev.logickoder.geofence.ui.screens.WelcomeScreen
import dev.logickoder.geofence.ui.theme.KodeCampTheme
import dev.logickoder.geofence.ui.theme.Padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeCampTheme {
                val modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding)

                val locations = remember { mutableStateListOf<LatLng>() }

                var screen by remember { mutableStateOf(0) }
                val screens = listOf<@Composable () -> Unit>(
                    {
                        WelcomeScreen(
                            modifier = modifier,
                            onStart = {
                                ++screen
                            }
                        )
                    },
                    {
                        SelectLocationsScreen(
                            modifier = modifier,
                            selectedLocations = locations.size,
                            onLocationSelected = {
                                locations += it
                            },
                            clearLocations = {
                                locations.removeIf { true }
                            },
                            onNext = {
                                ++screen
                            }
                        )
                    },
                    {
                        GeofenceScreen(
                            modifier = modifier,
                            locations = locations,
                        )
                    },
                )

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.app_name))
                            }
                        )
                    },
                    content = {
                        screens[screen].invoke()
                    }
                )
            }
        }
    }
}
