package com.example.circolariitis

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import io.socket.client.Socket
import io.socket.emitter.Emitter

class BackgroundServiceSocket : Service() {

    private lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()
        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        mSocket = SocketHandler.getSocket()

        mSocket.on("update",Emitter.Listener {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


}