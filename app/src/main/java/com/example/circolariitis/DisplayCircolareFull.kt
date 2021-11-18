package com.example.circolariitis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

class DisplayCircolareFull : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_circolare_full)

        val id = intent.getIntExtra("id",0)
        Toast.makeText(this,id.toString(),Toast.LENGTH_LONG).show()
    }
}