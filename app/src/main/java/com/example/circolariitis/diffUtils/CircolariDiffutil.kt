package com.example.circolariitis.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.circolariitis.dataClasses.CircolarePreview

class CircolariDiffutil (
    private val oldList: List<CircolarePreview>,
    private val newList: List<CircolarePreview>
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
            oldList[oldItemPosition].title != newList[newItemPosition].title -> return false
            //oldList[oldItemPosition].description != newList[newItemPosition].description -> return false
            else -> return true
        }
    }

}