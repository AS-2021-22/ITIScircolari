package com.example.circolariitis

class GLOBALS {
    companion object{ // static public
        val PROTOCOL : String = "https://"
        val SERVER_ADDRESS : String = "circolariitis.herokuapp.com"
        val PORT : String = ""
        val POST_CIRCOLARI : String = PROTOCOL + SERVER_ADDRESS + PORT + "/circolari"
        val POST_FILTRI : String = PROTOCOL + SERVER_ADDRESS + PORT + "/filters"
        val GET_CIRCOLARE : String = PROTOCOL + SERVER_ADDRESS + PORT + "/circolari/"
    }
}