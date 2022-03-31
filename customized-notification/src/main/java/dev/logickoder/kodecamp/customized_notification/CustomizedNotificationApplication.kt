package dev.logickoder.kodecamp.customized_notification

import android.app.Application


class CustomizedNotificationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        baseContext.createNotificationChannel()
    }
}