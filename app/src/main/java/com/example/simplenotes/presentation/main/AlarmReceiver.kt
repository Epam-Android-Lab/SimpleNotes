package com.example.simplenotes.presentation.main

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.simplenotes.R
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, intent)
    }

    private fun showNotification(context: Context, intent: Intent) {

        val pendingIntent = NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.nav_main)
                .setArguments(intent.getBundleExtra(TASK_ID))
                .setDestination(R.id.taskShowFragment)
                .createPendingIntent()

        var channel = ""

        when (intent.getStringExtra("type")) {
            "DEADLINE_ID" -> {
                channel = DEADLINE_CHANNEL
            }
            "REMINDER_ID" -> {
                channel = REMINDER_CHANNEL
            }
        }

        val notification = NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.sample_icon)
                .setContentTitle(intent.getStringExtra(TITLE_NAME))
                .setContentText(intent.getStringExtra(DESC_NAME))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setColor(Color.GREEN)

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(intent.getIntExtra(NOTIFICATION_ID, Random.nextInt()), notification.build())
    }

    companion object {
        private const val DEADLINE_CHANNEL = "DEADLINE_CHANNEL"
        private const val REMINDER_CHANNEL = "REMINDER_CHANNEL"
        private const val TITLE_NAME = "TITLE_NAME"
        private const val DESC_NAME = "DESC_NAME"
        private const val TASK_ID = "TASK_ID"
        private const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }
}