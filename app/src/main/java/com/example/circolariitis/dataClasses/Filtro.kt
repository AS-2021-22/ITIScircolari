package com.example.circolariitis.dataClasses

data class Filtro(
    var id:Int,
    var text: String,
    var active: Boolean
){
    override fun toString(): String {
        return "{id:$id,text.$text,active:$active}"
    }
}
