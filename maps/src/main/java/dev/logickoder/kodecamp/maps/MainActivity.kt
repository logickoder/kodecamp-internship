package dev.logickoder.kodecamp.maps

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dev.logickoder.kodecamp.maps.ui.shared.components.MapContainer
import dev.logickoder.kodecamp.maps.ui.theme.AppTheme
import dev.logickoder.kodecamp.maps.ui.theme.KodeCampTheme

class MainActivity : ComponentActivity() {

    private lateinit var locationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(this)

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
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
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
                        val markers = remember {
                            mutableStateListOf<Location>()
                        }
                        LaunchedEffect(key1 = Unit) {
                            locationClient.lastLocation.addOnSuccessListener {
                                if (it != null) markers.add(it)
                            }
                        }
                        MapContainer(
                            markers = markers,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppTheme.colors.background)
                        )
                    }
                )
            }
        }
    }
}

