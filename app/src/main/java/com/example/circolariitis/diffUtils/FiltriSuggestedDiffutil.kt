package com.example.circolariitis.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.circolariitis.dataClasses.Filtro

class FiltriSuggestedDiffutil(
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
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].text != newList[newItemPosition].text -> false
            oldList[oldItemPosition].active != newList[newItemPosition].active -> false
            else -> true
        }
    }

}