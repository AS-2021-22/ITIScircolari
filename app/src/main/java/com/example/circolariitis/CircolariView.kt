package com.example.circolariitis

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CircolariView : RecyclerView.Adapter<CircolariView.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener

    }

    private var circolari = Collections.emptyList<CircolarePreview>()

    inner class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var id: TextView = itemView.findViewById(R.id.number)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircolariView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.circolare_preview, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: CircolariView.ViewHolder, position: Int) {
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
        diffResult.dispatchUpdatesTo(this@CircolariView)
    }
}