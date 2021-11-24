package com.example.circolariitis.background

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.circolariitis.MainActivity
import com.example.circolariitis.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*

class BackgroundServiceSocket : Service() {

    private val channel_name = "canale nuova circolare"
    private val channel_description = "questo canale contiene le nuove circolari emesse dal server"
    private val CHANNEL_ID = "jc349cj9c494"
    private val gson = Gson()
    
    private lateinit var pendingIntent: PendingIntent
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filters: MutableList<String> // taken from the memory

    private data class UpdateCircolareNotification(
        var id: Number = -1,
        var title: String = "No title",
        var tags: List<String> = emptyList()
    ){
        override fun toString(): String {
            return "{id:$id,title:$title,tags:${tags.toString()}}"
        }
    }

    private lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = this.getSharedPreferences("filters", Context.MODE_PRIVATE)
        val stringFilterList = sharedPreferences.getString("filters","[]")
        filters = try{
            val typeListFilterFromString = object : TypeToken<MutableList<String>>() {}.type
            (gson.fromJson(stringFilterList,typeListFilterFromString) as MutableList<String>)
        }catch(e: Exception){
            Collections.emptyList()
        }

        createNotificationChannel()

        val intentNotification = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, 0)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()
        mSocket.on("update",Emitter.Listener { args ->

            val data: UpdateCircolareNotification = gson.fromJson(args[0].toString(), UpdateCircolareNotification::class.java)
            /*
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this,data.tags.toString(),Toast.LENGTH_LONG).show()
            }*/

            if(data.tags.contains("tutti") || hasSomethingInCommon(data.tags,filters)){
                val notification = createNotification("Nuova circolare N: ${data.id}",data.title, pendingIntent)

                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(UUID.randomUUID().clockSequence(), notification)
                }
            }
        })

    }

    private fun hasSomethingInCommon(l1: List<String>, l2: List<String>): Boolean {
        for(a in l1) if (l2.contains(a)) return true
        return false
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