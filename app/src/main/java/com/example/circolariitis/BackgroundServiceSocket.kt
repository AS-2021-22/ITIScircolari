package com.example.circolariitis

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter

class BackgroundServiceSocket : Service() {

    val gson = Gson()

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

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, data.title, Toast.LENGTH_LONG).show()
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