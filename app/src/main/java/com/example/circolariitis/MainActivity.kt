package com.example.circolariitis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.dataClasses.CircolarePreview
import com.example.circolariitis.databinding.ActivityMainBinding
import com.example.circolariitis.fragments.FilterDialogFragment
import com.example.circolariitis.recycleView.CircolariView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap
import android.app.ActivityManager
import com.example.circolariitis.background.BackgroundServiceSocket


class MainActivity : AppCompatActivity() {

    private lateinit var activityViewElements : ActivityMainBinding // V -> view
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: CircolariView
    private var filters : List<String> = Collections.emptyList()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private var listCircolariFromServer: List<CircolarePreview> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewElements = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityViewElements.root)

        //setup the recycle view
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CircolariView()
        activityViewElements.circolariView.layoutManager = layoutManager
        activityViewElements.circolariView.adapter = adapter

        adapter.setOnItemClickListener(object: CircolariView.OnItemClickListener{
            override fun onItemClick(position: Int) {
                showCircolareFull(listCircolariFromServer[position].id)
            }
        })

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

        val intentService = Intent(this, BackgroundServiceSocket::class.java)
        startService(intentService)

        //Toast.makeText(this,this.isServiceRunning(BackgroundServiceSocket::class.java).toString(),Toast.LENGTH_LONG).show()

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

    @Suppress("DEPRECATION")
    fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
        return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }
    }
}