package dev.logickoder.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import dev.logickoder.geofence.utils.AppState


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val event = GeofencingEvent.fromIntent(intent ?: return).also {
            if (it.hasError()) {
                Log.e(TAG, GeofenceStatusCodes.getStatusCodeString(it.errorCode))
                return
            }
        }
        when (val transition = event.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER, Geofence.GEOFENCE_TRANSITION_DWELL -> {
                // handle the transition
                AppState.transitions = event.triggeringGeofences
            }
            else -> {
                Log.e(TAG, "Invalid geofence transition: $transition")
            }
        }
    }

    companion object {
        val TAG = GeofenceBroadcastReceiver::class.simpleName
    }
}
