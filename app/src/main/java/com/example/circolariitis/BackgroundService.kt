package com.example.circolariitis

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class BackgroundService : IntentService(BackgroundService::class.simpleName) {

    private val channel_name = "nuova circolare"
    private val channel_description = "è stata aggunta una nuova circolare per te su ITIScircolari"
    private val CHANNEL_ID = "123"
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val intentNotification = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, 0)
    }

    override fun onHandleIntent(intent: Intent?) {
        // Gets data from the incoming Intent
        val dataString = intent?.dataString
        // Do work here, based on the contents of dataString

        val notificationId = 0

        val notification = createNotification("this is the notification","nuova circolare per te inserita", pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notification)
        }
    }

    private fun createNotification(title:String,text:String, intent: PendingIntent?): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.itislogo)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build() //in case of misfunction move .build below
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channel_name
            val descriptionText = channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}