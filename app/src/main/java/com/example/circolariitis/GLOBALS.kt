package com.example.circolariitis

object GLOBALS {

        private const val PROTOCOL : String = "http://"
        private const val SERVER_ADDRESS : String = "192.168.5.48" //"circolariitis.herokuapp.com"
        private const val PORT : String = ":5000"
        const val SERVER: String = "$PROTOCOL$SERVER_ADDRESS$PORT/"
        const val POST_CIRCOLARI : String = SERVER + "circolari"
        const val POST_FILTRI : String = SERVER + "filters"
        const val GET_CIRCOLARE : String = SERVER + "circolari/"

}