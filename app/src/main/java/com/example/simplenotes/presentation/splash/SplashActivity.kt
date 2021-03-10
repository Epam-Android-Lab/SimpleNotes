package com.example.simplenotes.presentation.splash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplenotes.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        createNotificationDeadlineChannel()
        createNotificationReminderChannel()
    }

    private fun createNotificationDeadlineChannel() {

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(DEADLINE_CHANNEL, "Deadline alarm", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Task deadline notification"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationReminderChannel() {

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(REMINDER_CHANNEL, "Reminder alarm", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Task reminder notification"
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val DEADLINE_CHANNEL = "DEADLINE_CHANNEL"
        private const val REMINDER_CHANNEL = "REMINDER_CHANNEL"
    }
}