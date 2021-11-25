package com.example.circolariitis.background

import com.example.circolariitis.GLOBALS
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

// this is copied from internet, it contains all for the socket connection

object SocketHandler {
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(GLOBALS.SERVER)
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}