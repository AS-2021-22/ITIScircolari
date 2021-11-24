package com.example.circolariitis.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.circolariitis.GLOBALS
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro
import com.example.circolariitis.recycleView.FiltriActiveView
import com.example.circolariitis.recycleView.FiltriSuggestionView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class FilterDialogFragment : DialogFragment() {

    private val gson: Gson = Gson()
    private lateinit var sharedPreferences: SharedPreferences

    private var filtriSuggested: MutableList<Filtro> = mutableListOf()
    private var filtriActive: MutableList<Filtro> = mutableListOf()

    private lateinit var adapterSuggestedFilters: FiltriSuggestionView
    private lateinit var adapterActiveFilters: FiltriActiveView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.filter_dialog_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("filters", Context.MODE_PRIVATE) ?: return
        val recyclerViewSuggestedFilters = view.findViewById<RecyclerView>(R.id.recycleViewSuggestedFilter)
        adapterSuggestedFilters = FiltriSuggestionView()
        adapterSuggestedFilters.setOnItemClickListener(object: FiltriSuggestionView.OnItemClickListener{
            override fun onItemClick(position: Int) {
                filtriSuggested[position].active = !filtriSuggested[position].active
                if(filtriSuggested[position].active){
                    filtriActive.add(filtriSuggested[position])
                    adapterActiveFilters.setData(filtriActive.toList())

                } else {
                    filtriActive.remove(filtriSuggested[position])
                    adapterActiveFilters.setData(filtriActive.toList())

                }
                adapterSuggestedFilters.setData(filtriSuggested.toList())
                adapterSuggestedFilters.notifyItemChanged(position)

                //Toast.makeText(context, "${filtriSuggested[position].active}", Toast.LENGTH_LONG).show()
            }
        })
        recyclerViewSuggestedFilters.layoutManager = GridLayoutManager(activity,3)
        recyclerViewSuggestedFilters.adapter = adapterSuggestedFilters


        // recycle view ActiveFilters
        val recyclerViewActiveFilters = view.findViewById<RecyclerView>(R.id.recycleViewActiveFilters)
        adapterActiveFilters = FiltriActiveView()
        adapterActiveFilters.setOnItemClickListener(object: FiltriActiveView.OnItemClickListener{
            override fun onItemClick(activeFilter: Filtro, position: Int) {
                filtriActive.remove(activeFilter)
                adapterActiveFilters.setData(filtriActive.toList())

                val n = filtriSuggested.indexOfFirst { it.id == activeFilter.id }
                if(n != -1){
                    filtriSuggested[n].active = false
                    adapterSuggestedFilters.setData(filtriSuggested.toList())
                    adapterSuggestedFilters.notifyItemChanged(n)
                }

            }
        })
        recyclerViewActiveFilters.layoutManager = LinearLayoutManager(activity)
        recyclerViewActiveFilters.adapter = adapterActiveFilters

        loadFiltri()

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnSuggestion = view.findViewById<Button>(R.id.btnSuggestion)
        val editTextYourFilter = view.findViewById<EditText>(R.id.filtroET)
        val addBtn = view.findViewById<Button>(R.id.addBtn)
        
        editTextYourFilter.setImeActionLabel("Save filter",KeyEvent.KEYCODE_ENTER)
        addBtn.setOnClickListener {
            val newFilterName: String = editTextYourFilter.text.toString()
            if(filtriActive.filter{it.text == newFilterName} == emptyList<Filtro>()) {
                filtriActive.add(Filtro(filtriActive.size,newFilterName,true))
                adapterActiveFilters.setData(filtriActive.toList())

                // hide soft keyboard programmatically
                hideKeyboard()
                // clear focus and hide cursor from edit text
                editTextYourFilter.clearFocus()
                editTextYourFilter.isCursorVisible = false
            }
        }

        btnSuggestion.setOnClickListener {
            if(recyclerViewSuggestedFilters.visibility == VISIBLE) recyclerViewSuggestedFilters.visibility = GONE
            else recyclerViewSuggestedFilters.visibility = VISIBLE
        }

        btnSave.setOnClickListener {

            // send back to main activity
            val result = mutableListOf<String>()
            for(f: Filtro in filtriActive)result.add(f.text)
            setFragmentResult("requestKey", bundleOf("filtersAsList<String>" to result))

            //val filtriTrue: List<Filtro> = filtriSuggested.filter { it.active }
            var listOutput = "["
            for (filtro in filtriActive) {
                listOutput += filtro.text + ','
            }
            //filtriTrue.forEach { f -> listOutput += "${f.text}," }
            listOutput = listOutput.substring(0,listOutput.length - 1) //removing last ,
            listOutput += ']'

            //Toast.makeText(activity,listOutput,Toast.LENGTH_LONG).show()

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
        val ffm: List<String> = filtriFromMemory()
        filtriPOST(ffm)
    }

    private fun filtriFromMemory() : List<String>{
        val stringFilterList = sharedPreferences.getString("filters","[]") //read
        val typeListFiltroFromString = object : TypeToken<List<String>>() {}.type //define type
        val ffm : List<String> = gson.fromJson(stringFilterList,typeListFiltroFromString) //cast
        ffm.forEach { filtriActive.add(Filtro(UUID.randomUUID().clockSequence(),it,true)) } //add filter
        adapterActiveFilters.setData(filtriActive) // puts in the RV
        return ffm
    }

    private fun filtriPOST(filtriFromMemory: List<String>){
        val queueP = Volley.newRequestQueue(activity)
        val stringReqP : StringRequest =
            object : StringRequest(
                Method.POST,
                GLOBALS.POST_FILTRI,
                Response.Listener { response ->
                    val strResp = response.toString() // get string response
                    val typeListString = object : TypeToken<List<String>>() {}.type // define type
                    val listFiltriFromServer: List<String> = gson.fromJson(strResp,typeListString) // cast the list of suggested filters

                    listFiltriFromServer.forEach {
                        var isActive = false
                        if(filtriFromMemory.contains(it)) isActive = true // if the filter was in the active
                        filtriSuggested.add(Filtro(UUID.randomUUID().clockSequence(),it,isActive))
                    }
                    adapterSuggestedFilters.setData(filtriSuggested.toList())
                    adapterActiveFilters.setData(filtriActive.toList())
                },
                Response.ErrorListener { err ->
                    Log.println(Log.ERROR,"error",err.toString())
                }
            ){}
        queueP.add(stringReqP)
    }

    private fun hideKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}