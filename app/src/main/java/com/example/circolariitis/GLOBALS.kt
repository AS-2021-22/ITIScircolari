package com.example.circolariitis

object GLOBALS {

        private const val PROTOCOL : String = "https://"
        private const val SERVER_ADDRESS : String = "circolariitis.herokuapp.com" //"circolariitis.herokuapp.com"
        private const val PORT : String = ""
        const val SERVER: String = "$PROTOCOL$SERVER_ADDRESS$PORT/"
        const val POST_CIRCOLARI : String = SERVER + "circolari"
        const val POST_FILTRI : String = SERVER + "filters"
        const val GET_CIRCOLARE : String = SERVER + "circolari/"

}