package com.example.circolariitis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class FiltriView : RecyclerView.Adapter<FiltriView.ViewHolder>() {

    private var filtri = emptyList<Filtro>()

    fun getFiltri () : List<Filtro>{
        return filtri
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var filtro = itemView.findViewById<CheckBox>(R.id.filter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_element,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.filtro.text = filtri[position].text
        holder.filtro.isChecked = filtri[position].active

        holder.filtro.setOnCheckedChangeListener { _, isChecked ->
            filtri[position].active = isChecked
        }
    }

    override fun getItemCount(): Int {
        return filtri.size
    }

    fun setData(newList: List<Filtro>){
        val diffutil = FiltriDiffutil(filtri,newList)
        val diffResult = DiffUtil.calculateDiff(diffutil)
        filtri = newList
        diffResult.dispatchUpdatesTo(this)
    }
}