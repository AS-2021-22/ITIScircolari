package com.example.circolariitis.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.circolariitis.dataClasses.Filtro

class FiltriDiffutil(
    private val oldList: List<Filtro>,
    private val newList: List<Filtro>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if(oldList[oldItemPosition].id == newList[newItemPosition].id) return true
        else return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> return false
            oldList[oldItemPosition].text != newList[newItemPosition].text -> return false
            oldList[oldItemPosition].active != newList[newItemPosition].active -> return false
            else -> return true
        }
    }

}