package com.example.simplenotes.presentation.main

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.simplenotes.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {

        val intent = Intent(context, MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(FROM_NOTIFICATION_KEY, true)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.sample_icon)
                .setContentTitle("Title")
                .setContentText("Text of notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.sample_icon))
                //.setDefaults(Notification.DEFAULT_SOUND)
                //.setColor(Color.RED)
                //.setStyle(NotificationCompat.BigTextStyle().bigText("text of notification"))

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val FROM_NOTIFICATION_KEY = "FROM_NOTIFICATION_KEY"
        private const val NOTIFICATION_ID = 1
    }

}