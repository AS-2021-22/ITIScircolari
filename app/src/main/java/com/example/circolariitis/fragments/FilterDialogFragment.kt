package com.example.circolariitis.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.GLOBALS
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro
import com.example.circolariitis.recycleView.FiltriView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FilterDialogFragment : DialogFragment() {

    private val gson: Gson = Gson()
    private lateinit var sharedPreferences: SharedPreferences
    private var filtri: MutableList<Filtro> = mutableListOf()

    private lateinit var adapter: FiltriView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.filter_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("filters", Context.MODE_PRIVATE) ?: return

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewFilter)

        adapter = FiltriView()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        loadFiltri()

        val btn = view.findViewById<Button>(R.id.btnSave)
        btn.setOnClickListener {
            val result = mutableListOf<String>()
            filtri = adapter.getFiltri().toMutableList()
            filtri.forEach { f -> if(f.active)result.add(f.text)}
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))

            val filtriTrue: List<Filtro> = filtri.filter { it.active }
            var listOutput = "["
            filtriTrue.forEach { f -> listOutput += "${f.text}," }
            listOutput = listOutput.substring(0,listOutput.length - 1) //removing last ,
            listOutput += ']'

            with (sharedPreferences.edit()) {
                clear()
                putString("filters", listOutput)
                apply()
            }
            //val writtenString: String? = sharedPreferences.getString("test","writtenString") //if the string doesn't exists takes the second value
            dismiss() //close fragment
        }



    }

    private fun loadFiltri(){
        filtriPOST()
    }

    private fun filtriPOST(){
        val queueP = Volley.newRequestQueue(this.activity)

        val stringFilterList = sharedPreferences.getString("filters","[]")
        val mlistFiltriFromMemory: MutableList<String> = try{
            val typeListFiltroFromString = object : TypeToken<List<String>>() {}.type
            (gson.fromJson(stringFilterList,typeListFiltroFromString) as List<String>).toMutableList()
        }catch(e: Exception){
            emptyList<String>().toMutableList()
        }
        val stringReqP : StringRequest =
            object : StringRequest(
                Method.POST,
                GLOBALS.POST_FILTRI,
                Response.Listener { response ->
                    // response
                    val strResp = response.toString()
                    val typeListFiltro = object : TypeToken<List<String>>() {}.type
                    val listFiltriFromServer: List<String> = gson.fromJson(strResp,typeListFiltro)

                    for (f: String in listFiltriFromServer){

                        val indexFound: String? = try{
                            mlistFiltriFromMemory.filter{ it == f}[0]
                            //Log.d("list",mlistFiltriFromMemory.toString())
                        } catch(e:Exception){
                            null
                        }
                        if(indexFound != null) {
                            mlistFiltriFromMemory.remove(indexFound)
                            filtri.add(Filtro(filtri.size,indexFound,true))
                        }
                        else filtri.add(Filtro(filtri.size,f,false))
                    }
                    adapter.setData(filtri)
                },
                Response.ErrorListener { err ->
                    Log.println(Log.ERROR,"error",err.toString())
                    //adapter.setData(filtri)
                }
            ){}
        queueP.add(stringReqP)
    }

}