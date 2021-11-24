package com.example.circolariitis.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro
import com.example.circolariitis.diffUtils.FiltriSuggestedDiffutil

class FiltriActiveView : RecyclerView.Adapter<FiltriActiveView.ViewHolder>()  {

    private var activeFilters: List<Filtro> = emptyList()
    private lateinit var mListener : FiltriActiveView.OnItemClickListener

    inner class ViewHolder(itemView: View, listener: FiltriActiveView.OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val activeFilterName = itemView.findViewById<TextView>(R.id.activeFilterName)
        val deleteIMG = itemView.findViewById<ImageView>(R.id.deleteIMG)

        init{
            deleteIMG.setOnClickListener{
                listener.onItemClick(activeFilters[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_active_element,parent,false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.activeFilterName.text = activeFilters[position].text
    }

    override fun getItemCount(): Int {
        return activeFilters.size
    }

    interface OnItemClickListener{
        fun onItemClick(activeFilter: Filtro, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun setData(newList: List<Filtro>){
        val diffutil = FiltriSuggestedDiffutil(activeFilters,newList)
        val diffResult = DiffUtil.calculateDiff(diffutil)
        activeFilters = newList
        diffResult.dispatchUpdatesTo(this)
    }
}