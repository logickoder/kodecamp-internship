package dev.logickoder.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dev.logickoder.geofence.ui.screens.GeofenceScreen
import dev.logickoder.geofence.ui.screens.SelectLocationsScreen
import dev.logickoder.geofence.ui.screens.WelcomeScreen
import dev.logickoder.geofence.ui.theme.KodeCampTheme
import dev.logickoder.geofence.ui.theme.Padding
import dev.logickoder.geofence.utils.AppState
import dev.logickoder.geofence.utils.getGeofencingRequest

class MainActivity : ComponentActivity() {

    private lateinit var locationClient: FusedLocationProviderClient
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

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        setContent {
            KodeCampTheme {

                @Composable
                fun RequireLocation() = Dialog(
                    onDismissRequest = { /*TODO*/ },
                    content = {
                        Text("Location Permission Required")
                    }
                )

                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
                PermissionsRequired(
                    multiplePermissionsState = permissionState,
                    permissionsNotGrantedContent = {
                        LaunchedEffect(key1 = Unit) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                        RequireLocation()
                    },
                    permissionsNotAvailableContent = { RequireLocation() },
                    content = {
                        val locations = remember { mutableStateListOf<LatLng>() }
                        var screen by remember { mutableStateOf(0) }
                        val screens = screens(
                            locations = locations,
                            onScreenChange = {
                                screen = it
                            }
                        )

                        LaunchedEffect(key1 = Unit) {
                            locationClient.lastLocation.addOnSuccessListener {
                                if (it != null)
                                    AppState.location = LatLng(it.latitude, it.longitude)
                            }
                        }

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
                )
            }
        }
    }

    @Composable
    private fun Screen(
        screen: Int,
        onScreenChange: (Int) -> Unit,
        content: @Composable () -> Unit,
    ) {
        Box {
            BackHandler(
                enabled = screen > 0,
                onBack = {
                    onScreenChange(screen - 1)
                }
            )
            content()
        }
    }

    @SuppressLint("MissingPermission")
    private fun screens(
        locations: MutableList<LatLng>,
        onScreenChange: (Int) -> Unit,
    ): List<@Composable () -> Unit> {

        val modifier = Modifier
            .fillMaxSize()
            .padding(Padding)
        return listOf<@Composable () -> Unit>(
            {
                WelcomeScreen(
                    modifier = modifier,
                    onStart = {
                        onScreenChange(1)
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
                        onScreenChange(2)
                    }
                )
            },
            {
                LaunchedEffect(Unit) {
                    geofencingClient.addGeofences(
                        locations.getGeofencingRequest(), geofencePendingIntent
                    ).addOnSuccessListener {
                        Log.d(TAG, "Geofencing successfully added")
                    }
                    locations.forEach {
                        Log.d(TAG, "screens: $it")
                    }
                }
                GeofenceScreen(
                    modifier = modifier,
                    locations = locations,
                    updateLocations = { updatedLocations ->
                        locations.removeAll { it !in updatedLocations }
                    }
                )
            }
        ).mapIndexed { screen, content ->
            {
                Screen(
                    screen = screen,
                    onScreenChange = onScreenChange,
                    content = content,
                )
            }
        }
    }

    private val TAG = this::class.simpleName
}
