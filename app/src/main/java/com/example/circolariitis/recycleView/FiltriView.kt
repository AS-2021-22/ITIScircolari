package com.example.circolariitis.recycleView

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.circolariitis.diffUtils.FiltriDiffutil
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.Filtro

class FiltriView : RecyclerView.Adapter<FiltriView.ViewHolder>() {
    private lateinit var context: Context
    private var filtri = emptyList<Filtro>()

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun getFiltri () : List<Filtro>{
        return filtri
    }

    inner class ViewHolder(itemView: View, listener: FiltriView.OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val filtro = itemView.findViewById<TextView>(R.id.filterTV)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_element,parent,false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.filtro.text = filtri[position].text
        if(filtri[position].active) holder.filtro.background = AppCompatResources.getDrawable(context,R.drawable.rounded_textview_green)
        else holder.filtro.background = AppCompatResources.getDrawable(context,R.drawable.rounded_textview_red)

        /*holder.filtro.isChecked = filtri[position].active

        holder.filtro.setOnCheckedChangeListener { _, isChecked ->
            filtri[position].active = isChecked
        }*/
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