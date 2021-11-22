package com.example.circolariitis

object GLOBALS {

        private const val PROTOCOL : String = "https://"
        private const val SERVER_ADDRESS : String = "circolariitis.herokuapp.com"
        private const val PORT : String = ""
        const val POST_CIRCOLARI : String = "$PROTOCOL$SERVER_ADDRESS$PORT/circolari"
        const val POST_FILTRI : String = "$PROTOCOL$SERVER_ADDRESS$PORT/filters"
        const val GET_CIRCOLARE : String = "$PROTOCOL$SERVER_ADDRESS$PORT/circolari/"

}