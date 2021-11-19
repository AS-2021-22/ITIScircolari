package com.example.circolariitis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.dataClasses.Circolare
import com.example.circolariitis.databinding.ActivityDisplayCircolareFullBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DisplayCircolareFull : AppCompatActivity() {

    private val gson = Gson()
    private lateinit var activityViewElements : ActivityDisplayCircolareFullBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_circolare_full)

        activityViewElements = ActivityDisplayCircolareFullBinding.inflate(layoutInflater)
        setContentView(activityViewElements.root)

        val id = intent.getIntExtra("id",0)

        //Toast.makeText(this,id,Toast.LENGTH_LONG).show()

        val queue = Volley.newRequestQueue(this)
        val stringReq : StringRequest =
            object : StringRequest(
                Method.GET,
                GLOBALS.GET_CIRCOLARE + "$id",
                Response.Listener { response ->
                    Log.d("data", response.toString())
                    val strResp = response.toString()
                    val mutableListTutorialType = object : TypeToken<Circolare>() {}.type
                    val circolare: Circolare = gson.fromJson(strResp, mutableListTutorialType)
                    activityViewElements.cnumber.text = circolare.id.toString()
                    activityViewElements.ctitle.text = circolare.title
                    activityViewElements.cdescription.text = circolare.description
                },
                Response.ErrorListener { error ->
                    Log.println(Log.ERROR,"error",error.toString())
                }
            ){}
        queue.add(stringReq)
    }
}