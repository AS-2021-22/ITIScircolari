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

    private val gson: Gson = Gson() // JSON converter
    private lateinit var sharedPreferences: SharedPreferences // memory reader

    private var filtriSuggested: MutableList<Filtro> = mutableListOf() // list of suggested filters
    private var filtriActive: MutableList<Filtro> = mutableListOf() // list of active filters

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

        // ************ get components from the view ***************//
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnSuggestion = view.findViewById<Button>(R.id.btnSuggestion)
        val editTextYourFilter = view.findViewById<EditText>(R.id.filtroET)
        val addBtn = view.findViewById<Button>(R.id.addBtn)
        val recyclerViewSuggestedFilters = view.findViewById<RecyclerView>(R.id.recycleViewSuggestedFilter)
        val recyclerViewActiveFilters = view.findViewById<RecyclerView>(R.id.recycleViewActiveFilters)

        sharedPreferences = activity?.getSharedPreferences("filters", Context.MODE_PRIVATE) ?: return

        //*****************************+++ setting up suggested filters RV ******************//
        adapterSuggestedFilters = FiltriSuggestionView()
        adapterSuggestedFilters.setOnItemClickListener(object: FiltriSuggestionView.OnItemClickListener{
            override fun onItemClick(position: Int) {
                filtriSuggested[position].active = !filtriSuggested[position].active // change the filter state
                if(filtriSuggested[position].active){ // if active == true ..... add to active
                    filtriActive.add(filtriSuggested[position])
                    adapterActiveFilters.setData(filtriActive.toList())
                } else { // if active == false .... remove from active
                    filtriActive.remove(filtriSuggested[position])
                    adapterActiveFilters.setData(filtriActive.toList())

                }
                // update the suggestedFilters RV with the new active state
                adapterSuggestedFilters.setData(filtriSuggested.toList())
                adapterSuggestedFilters.notifyItemChanged(position)
            }
        })
        recyclerViewSuggestedFilters.apply {
            layoutManager = GridLayoutManager(activity,3)
            adapter = adapterSuggestedFilters
        }

        //*****************************+++ setting up active filters RV ******************//
        adapterActiveFilters = FiltriActiveView()
        adapterActiveFilters.setOnItemClickListener(object: FiltriActiveView.OnItemClickListener{
            override fun onItemClick(activeFilter: Filtro, position: Int) { // when delete icon is clicked
                filtriActive.remove(activeFilter)
                adapterActiveFilters.setData(filtriActive.toList())

                // if the active filter was also in the suggested filters, it change the state in that RV
                val n = filtriSuggested.indexOfFirst { it.text == activeFilter.text }
                if(n != -1){
                    filtriSuggested[n].active = false
                    adapterSuggestedFilters.setData(filtriSuggested.toList())
                    adapterSuggestedFilters.notifyItemChanged(n)
                }
            }
        })
        recyclerViewActiveFilters.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = adapterActiveFilters
        }

        // ************** load filters (memory and server) *******//
        loadFiltri()

        // ****************** buttons listeners ***********************//

        editTextYourFilter.setImeActionLabel("Save filter",KeyEvent.KEYCODE_ENTER) // this line looks like it is not working
        addBtn.setOnClickListener { //this button if clicked save the new filter
            val newFilterName: String = editTextYourFilter.text.toString()
            if(filtriActive.filter{it.text == newFilterName} == emptyList<Filtro>()) { // check if there is no rendundace
                filtriActive.add(Filtro(UUID.randomUUID().toString(),newFilterName,true))
                adapterActiveFilters.setData(filtriActive.toList())

                // hide soft keyboard programmatically
                hideKeyboard()
                // clear focus and hide cursor from edit text
                editTextYourFilter.clearFocus()
                editTextYourFilter.isCursorVisible = false
            }
        }

        //************ show/hide suggestions****++++//
        btnSuggestion.setOnClickListener {
            if(recyclerViewSuggestedFilters.visibility == VISIBLE) recyclerViewSuggestedFilters.visibility = GONE
            else recyclerViewSuggestedFilters.visibility = VISIBLE
        }

        // *********** save filters on the memory and close the fragment ******//
        btnSave.setOnClickListener {

            // send back to main activity with a message in FragmentResult as List<String>
            val result = mutableListOf<String>()
            for(f: Filtro in filtriActive)result.add(f.text)
            setFragmentResult("requestKey", bundleOf("filtersAsList<String>" to result))

            //write on memorys
            with (sharedPreferences.edit()) {
                clear() // clear whatever was written before
                putString("filters", result.toString()) //put the string
                apply() //'commit'
            }
            dismiss() //close fragment
        }
    }

    // ***** loads data.... from memory and from the server *****************//
    private fun loadFiltri(){
        val ffm: List<String> = filtriFromMemory()
        filtriPOST(ffm)
    }

    private fun filtriFromMemory() : List<String>{
        val stringFilterList = sharedPreferences.getString("filters","[]") //read
        val typeListFiltroFromString = object : TypeToken<List<String>>() {}.type //define type
        val ffm : List<String> = gson.fromJson(stringFilterList,typeListFiltroFromString) //cast
        ffm.forEach { filtriActive.add(Filtro(UUID.randomUUID().toString(),it,true)) } //add filter
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
                        filtriSuggested.add(Filtro(UUID.randomUUID().toString(),it,isActive))
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