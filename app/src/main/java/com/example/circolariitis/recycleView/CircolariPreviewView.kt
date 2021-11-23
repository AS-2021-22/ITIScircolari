package com.example.circolariitis.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.circolariitis.diffUtils.CircolariDiffutil
import com.example.circolariitis.R
import com.example.circolariitis.dataClasses.CircolarePreview
import java.util.*

class CircolariPreviewView : RecyclerView.Adapter<CircolariPreviewView.ViewHolder>() {

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    private var circolari = Collections.emptyList<CircolarePreview>()

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var id: TextView = itemView.findViewById(R.id.number)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.circolare_preview_element, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = circolari[position].title
        holder.id.text = circolari[position].id.toString()
    }

    override fun getItemCount(): Int {
        return circolari.size
    }

    fun setData(newList: List<CircolarePreview>) {
        val diffutil = CircolariDiffutil(circolari, newList)
        val diffResult = DiffUtil.calculateDiff(diffutil)
        circolari = newList
        diffResult.dispatchUpdatesTo(this@CircolariPreviewView)
    }
}