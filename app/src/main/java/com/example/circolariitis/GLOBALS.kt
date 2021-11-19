package com.example.circolariitis

class GLOBALS {
    companion object{ // static public
        val PROTOCOL : String = "http://"
        val SERVER_ADDRESS : String = "192.168.5.77"
        val PORT : String = ":5000"
        val POST_CIRCOLARI : String = PROTOCOL + SERVER_ADDRESS + PORT + "/circolari"
        val POST_FILTRI : String = PROTOCOL + SERVER_ADDRESS + PORT + "/filters"
        val GET_CIRCOLARE : String = PROTOCOL + SERVER_ADDRESS + PORT + "/circolari/"
    }
}