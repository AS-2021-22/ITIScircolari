package com.example.circolariitis.dataClasses

//this is the full circolare dataclass (as in the DB)
data class Circolare(
    var id:Int,
    var title: String = "",
    var description: String = ""
)
