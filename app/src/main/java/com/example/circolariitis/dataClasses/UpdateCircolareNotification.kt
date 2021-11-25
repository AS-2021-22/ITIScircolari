package com.example.circolariitis.dataClasses

// this is the data that server socket sends to the client for the notification

data class UpdateCircolareNotification(
    var id: Number = -1,
    var title: String = "No title",
    var tags: List<String> = emptyList()
){
    override fun toString(): String {
        return "{id:$id,title:$title,tags:${tags.toString()}}"
    }
}
