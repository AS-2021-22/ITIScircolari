package com.example.circolariitis

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class BackgroundServiceSocket : Service() {

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this,"background process created",Toast.LENGTH_LONG).show()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
        //this service only display notifications
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Toast.makeText(this,"background process started",Toast.LENGTH_LONG).show()
        return START_STICKY
    }
}