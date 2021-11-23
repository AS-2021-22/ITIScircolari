package com.example.circolariitis.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro

class FiltriActiveView : RecyclerView.Adapter<FiltriActiveView.ViewHolder>()  {

    private var activeFilters: List<Filtro> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val activeFilterName = itemView.findViewById<TextView>(R.id.activeFilterName)
        val deleteIMG = itemView.findViewById<ImageView>(R.id.deleteIMG)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_active_element,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.activeFilterName.text = activeFilters[position].text
    }

    override fun getItemCount(): Int {
        return activeFilters.size
    }
}