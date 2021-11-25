package com.example.circolariitis.recycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.circolariitis.diffUtils.FiltriSuggestedDiffutil
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro

class FiltriSuggestionView : RecyclerView.Adapter<FiltriSuggestionView.ViewHolder>() {
    private lateinit var context: Context
    private var filtri = emptyList<Filtro>()

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val filtro : TextView = itemView.findViewById(R.id.filterTV)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_suggested_element,parent,false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.filtro.text = filtri[position].text
        if(filtri[position].active) holder.filtro.background = AppCompatResources.getDrawable(context,R.drawable.rounded_textview_green)
        else holder.filtro.background = AppCompatResources.getDrawable(context,R.drawable.rounded_textview_red)
    }

    override fun getItemCount(): Int {
        return filtri.size
    }

    fun setData(newList: List<Filtro>){
        val diffutil = FiltriSuggestedDiffutil(filtri,newList)
        val diffResult = DiffUtil.calculateDiff(diffutil)
        filtri = newList
        diffResult.dispatchUpdatesTo(this)
    }
}