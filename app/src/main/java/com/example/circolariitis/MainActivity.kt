package com.example.circolariitis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.dataClasses.CircolarePreview
import com.example.circolariitis.databinding.ActivityMainBinding
import com.example.circolariitis.fragments.FilterDialogFragment
import com.example.circolariitis.recycleView.CircolariPreviewView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap
import com.example.circolariitis.activities.DisplayCircolareFull
import com.example.circolariitis.background.BackgroundServiceSocket


class MainActivity : AppCompatActivity() {

    // **********************************+ VARABLES *************************************//

    private lateinit var activityViewElements : ActivityMainBinding // view for binding
    private lateinit var adapterPreviewCircolari: CircolariPreviewView //adapter for recycleview previewCircolari
    private var filters : List<String> = Collections.emptyList() //active filters
    private lateinit var sharedPreferences: SharedPreferences //to read the memory to keep data
    private val gson = Gson() //to parse string objects to whatever you need
    private var listCircolariFromServer: List<CircolarePreview> = emptyList() // circolari that server sends

    //********************************* INIT *****************************************************//

    override fun onCreate(savedInstanceState: Bundle?) {

        //************************** bind view components ****************************************+//
        super.onCreate(savedInstanceState)
        activityViewElements = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityViewElements.root)

        //******************+ setup recycle view ***********************************************++//
        adapterPreviewCircolari = CircolariPreviewView() // define adapter
        activityViewElements.circolariView.layoutManager = LinearLayoutManager(this@MainActivity) // set layout menager
        activityViewElements.circolariView.adapter = adapterPreviewCircolari  // set A

        adapterPreviewCircolari.setOnItemClickListener(object: CircolariPreviewView.OnItemClickListener{ // setup onclick listener
            override fun onItemClick(position: Int) {
                showCircolareFull(listCircolariFromServer[position].id) // function that open the new activity
            }
        })

        //***************************** setup refresh layout to scroll up to reload data *************************************//
        activityViewElements.refreshLayout.setOnRefreshListener {
            circolariPOST()
            activityViewElements.refreshLayout.isRefreshing = false
        }

        //****************************** setup communication with fragment **********************************************//
        supportFragmentManager
            .setFragmentResultListener("requestKey", this) { _, bundle ->
                @Suppress("UNCHECKED_CAST")
                filters = bundle.get("filtersAsList<String>") as List<String>
                circolariPOST() // after the fragment is closed it updates the recycle view with the new filters
            }

        //************************************* get saved filters from the memory **************************************************//
        sharedPreferences = this.getSharedPreferences("filters", Context.MODE_PRIVATE) // open communcation

        val stringFilterList = sharedPreferences.getString("filters","[]") //take the value as a string, if is not defined the value is "[]"
        val typeListFilterFromString = object : TypeToken<List<String>>() {}.type // define the type to cast
        filters = gson.fromJson(stringFilterList,typeListFilterFromString) // geo cast

        val intentService = Intent(this, BackgroundServiceSocket::class.java) // create the service (onCreate is runned)
        startService(intentService) // start the service (onStartCommand is runned)

        //***************+ get circolari from the server *********************//
        circolariPOST()
    }


    // ******************************** MENU ***********************************++ //

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // create the menu
        menuInflater.inflate(R.menu.menu_filter,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // when an item is selected
        when(item.itemId){
            R.id.filterMenu -> openFilterDialog() // start the fragment
        }
        return super.onOptionsItemSelected(item)
    }

    // ****************************** ACTIVITIES and FRAGMENTS *************************************//

    private fun showCircolareFull(id : Int){ // start the
        val intent = Intent(this, DisplayCircolareFull::class.java).apply {
            putExtra("id",id )
        }
        startActivity(intent)
    }

    private fun openFilterDialog(){
        val dialog = FilterDialogFragment()
        dialog.show(supportFragmentManager, "Filter Dialog")
    }

    // **************************** SERVER REQUESTS ***********************************************//

    private fun circolariPOST(){

        val queueP = Volley.newRequestQueue(this)
        val stringReqP : StringRequest =
            object : StringRequest(
                Method.POST, // method
                GLOBALS.POST_CIRCOLARI, // url
                Response.Listener { response ->
                    val strResp = response.toString()
                    val mutableListCircolariPreview = object : TypeToken<List<CircolarePreview>>() {}.type
                    listCircolariFromServer = gson.fromJson(strResp, mutableListCircolariPreview)
                    adapterPreviewCircolari.setData(listCircolariFromServer)
                },
                Response.ErrorListener { error ->
                    Log.println(Log.ERROR,"error",error.toString())
                }
            )
            {
                override fun getParams(): Map<String, String> {
                    val paramsML: MutableMap<String, String> = HashMap()
                    var s = "["
                    for(f in filters){
                        s += '"'+f+'"' + ','
                    }
                    s = s.substring(0,s.length-1) + ']'
                    paramsML["filter"] = s
                    return paramsML
                }
            }
        queueP.add(stringReqP)
    }
}