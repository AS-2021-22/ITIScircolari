package com.example.circolariitis

import android.app.IntentService
import android.content.Intent

class BackgroundService : IntentService(BackgroundService::class.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        // Gets data from the incoming Intent
        val dataString = intent?.dataString
        // Do work here, based on the contents of dataString
    }
}