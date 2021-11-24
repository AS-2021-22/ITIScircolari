package com.example.circolariitis.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.circolariitis.dataClasses.CircolarePreview

class CircolariPreviewDiffutil (
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
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].title != newList[newItemPosition].title -> false
            else -> true
        }
    }

}