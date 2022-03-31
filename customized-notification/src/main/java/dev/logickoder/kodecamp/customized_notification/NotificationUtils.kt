package dev.logickoder.kodecamp.customized_notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val NotificationId = "1"

fun Context.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            NotificationId,
            getString(R.string.notification_title),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

fun Context.createNotification() {
    val text = getString(R.string.notification_text)
    val notification = NotificationCompat.Builder(this, NotificationId)
        .setContentTitle(getString(R.string.notification_title))
        .setContentText(text.split(Regex("\\s+")).take(2).joinToString(" "))
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(this).notify(1, notification)
}