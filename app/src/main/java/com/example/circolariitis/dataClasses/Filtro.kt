package com.example.circolariitis.dataClasses

//this is the filter class
data class Filtro(
    var id:Int,
    var text: String,
    var active: Boolean
){
    override fun toString(): String {
        return "{id:$id,text.$text,active:$active}"
    }
}
