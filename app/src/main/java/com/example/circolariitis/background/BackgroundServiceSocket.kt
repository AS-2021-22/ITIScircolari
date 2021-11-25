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
import com.example.circolariitis.dataClasses.UpdateCircolareNotification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket

class BackgroundServiceSocket : Service() {

    private val channelName = "canale nuova circolare"
    private val channelDescription = "questo canale contiene le nuove circolari emesse dal server"
    private val channelID = "jc349cj9c494"
    private val gson = Gson()
    
    private lateinit var pendingIntent: PendingIntent
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filters: List<String> // taken from the memory

    private var progressiveId: Int = 0

    private lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()


        // **********************************+++ read filters from memory *********************************//
        sharedPreferences = this.getSharedPreferences("filters", Context.MODE_PRIVATE)
        val stringFilterList = sharedPreferences.getString("filters","[]")
        val typeListFilterFromString = object : TypeToken<List<String>>() {}.type
        filters = gson.fromJson(stringFilterList,typeListFilterFromString)

        //************************************* starts notification menagement ***************************//
        createNotificationChannel()
        val intentNotification = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_IMMUTABLE)


        //********************************** starts socket connection ********************************//
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()
        mSocket.on("update") { args ->

            val data: UpdateCircolareNotification =
                gson.fromJson(args[0].toString(), UpdateCircolareNotification::class.java)
            /* //if it is needed to toast someting take this code
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this,data.tags.toString(),Toast.LENGTH_LONG).show()
            }*/

            if (data.tags.contains("tutti") || hasSomethingInCommon(data.tags, filters) || filters.toString() == "[]") {
                val notification =
                    createNotification("Nuova circolare N: ${data.id}", data.title, pendingIntent)

                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(progressiveId, notification)
                    progressiveId++
                }
            }
        }

    }


    // ************************ check if two arrays has an element in common ************************//
    private fun hasSomethingInCommon(l1: List<String>, l2: List<String>): Boolean {
        for(a in l1) if (l2.contains(a)) return true
        return false
    }

    // *********************+++ here is where you can set the communication between background and activity *******//
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //********************* when you start the background it return START_STYKY ******************++//
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY //when the application is closed this background is recreated
    }

    //*********************** generate the channel for the notification *************************/
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelName
            val descriptionText = channelDescription
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // ******************************** returns a builded notification ***********************//
    private fun createNotification(title:String,text:String, intent: PendingIntent?): Notification {
        return NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_school_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build() //in case of misfunction move .build below
    }
}