package com.example.circolariitis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    // TODO: implement a second activity !!!

    private lateinit var activityViewElements : ActivityMainBinding // V -> view
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: CircolariView
    private var filters : List<String> = Collections.emptyList()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private var listCircolariFromServer: List<CircolarePreview> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup the view binding
        activityViewElements = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityViewElements.root)
        //val view = V.root
        //setContentView(view)

        //setup the recycle view
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CircolariView()
        activityViewElements.circolariView.layoutManager = layoutManager
        activityViewElements.circolariView.adapter = adapter

        adapter.setOnItemClickListener(object: CircolariView.onItemClickListener{
            override fun onItemClick(position: Int) {
                showCircolareFull(listCircolariFromServer[position].id)
            }
        })

        //display connection state
        //if(isNetworkAvailable(this)) Toast.makeText(this,"Internet connection available", Toast.LENGTH_LONG).show()
        //else Toast.makeText(this,"No internet connection", Toast.LENGTH_LONG).show()

        activityViewElements.refreshLayout.setOnRefreshListener {
            circolariPOST()
            activityViewElements.refreshLayout.isRefreshing = false
        }

        supportFragmentManager
            .setFragmentResultListener("requestKey", this) { _, bundle ->
                // We use a String here, but any type that can be put in a Bundle is supported
                @Suppress("UNCHECKED_CAST")
                filters = bundle.get("bundleKey") as List<String>
                circolariPOST()
            }

        sharedPreferences = this.getSharedPreferences("filters", Context.MODE_PRIVATE)
        val stringFilterList = sharedPreferences.getString("filters","[]")
        val listFiltersFromMemory: List<String> = try{
            val typeListFilterFromString = object : TypeToken<List<String>>() {}.type
            (gson.fromJson(stringFilterList,typeListFilterFromString) as List<String>)
        }catch(e: Exception){
            Collections.emptyList()
        }
        filters = listFiltersFromMemory

        circolariPOST()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filter,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filterMenu -> openFilterDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCircolareFull(id : Int){
        val intent = Intent(this, DisplayCircolareFull::class.java).apply {
            putExtra("id",id )
        }
        startActivity(intent)
    }

    private fun circolariPOST(){
        val queueP = Volley.newRequestQueue(this)
        val stringReqP : StringRequest =
            object : StringRequest(
                Method.POST,
                GLOBALS.POST_CIRCOLARI,
                Response.Listener { response ->
                    Log.d("data", response.toString())
                    // response
                    val strResp = response.toString()
                    val mutableListTutorialType = object : TypeToken<List<CircolarePreview>>() {}.type
                    listCircolariFromServer = gson.fromJson(strResp, mutableListTutorialType)
                    adapter.setData(listCircolariFromServer)
                },
                Response.ErrorListener { error ->
                    Log.println(Log.ERROR,"error",error.toString())
                }
            )
            {
                override fun getParams(): Map<String, String> {
                    val paramsML: MutableMap<String, String> = HashMap()
                    //Change with your post params
                    //filter to string
                    var stringFilter = "["
                    filters.forEach { f -> stringFilter += "\"$f\"," }
                    stringFilter = stringFilter.substring(0,stringFilter.length-1)
                    stringFilter += "]"
                    paramsML["filter"] = stringFilter
                    return paramsML
                }
            }
        queueP.add(stringReqP)
    }

    private fun openFilterDialog(){
        val dialog = FilterDialogFragment()
        dialog.show(supportFragmentManager, "Filter Dialog")

    }

    //from internet

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
                else ->     false
            }
        }
        // For below 29 api
        /*
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }*/
        return false
    }
}