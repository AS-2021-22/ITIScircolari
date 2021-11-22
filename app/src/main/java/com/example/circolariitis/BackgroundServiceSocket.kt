package com.example.circolariitis

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter

class BackgroundServiceSocket : Service() {

    private val channel_name = "nuova circolare"
    private val channel_description = "è stata aggunta una nuova circolare per te su ITIScircolari"
    private val CHANNEL_ID = "123"
    private lateinit var pendingIntent: PendingIntent
    private var notificationId: Int = 0
    private val gson = Gson()

    private data class DataUpdate(
        var id: Number = -1,
        var title: String = "No title",
        var tags: List<String> = emptyList()
    )

    private lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()
        mSocket.on("update",Emitter.Listener { args ->

            val data: DataUpdate = gson.fromJson(args[0].toString(), DataUpdate::class.java)

            val notification = createNotification("Nuova circolare N: ${data.id}",data.title, pendingIntent)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, notification)
                notificationId ++
            }
        })

        createNotificationChannel()

        val intentNotification = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, 0)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY //when the application is closed this background is recreated
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

    private fun createNotification(title:String,text:String, intent: PendingIntent?): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_school_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build() //in case of misfunction move .build below
    }
}