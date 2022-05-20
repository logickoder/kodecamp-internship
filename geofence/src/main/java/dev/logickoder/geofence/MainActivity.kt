package dev.logickoder.geofence

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
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
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dev.logickoder.geofence.ui.screens.GeofenceScreen
import dev.logickoder.geofence.ui.screens.SelectLocationsScreen
import dev.logickoder.geofence.ui.screens.WelcomeScreen
import dev.logickoder.geofence.ui.theme.KodeCampTheme
import dev.logickoder.geofence.ui.theme.Padding
import dev.logickoder.geofence.utils.getGeofencingRequest

class MainActivity : ComponentActivity() {

    private lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, GeofenceBroadcastReceiver::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                else PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geofencingClient = LocationServices.getGeofencingClient(this)

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
                                while (locations.isNotEmpty())
                                    locations.removeAt(0)
                            },
                            onNext = {
                                geofencingClient.addGeofences(
                                    locations.getGeofencingRequest(), geofencePendingIntent
                                ).run {

                                }
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
