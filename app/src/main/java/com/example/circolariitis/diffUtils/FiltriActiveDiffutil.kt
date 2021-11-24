package com.example.circolariitis.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.circolariitis.dataClasses.Filtro

class FiltriActiveDiffutil (
    private val oldList: List<Filtro>,
    private val newList: List<Filtro>
) : DiffUtil.Callback()  {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        return oldList[p0].id == newList[p1].id
    }

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        return when{
            oldList[p0].id != newList[p1].id -> false
            oldList[p0].active != newList[p1].active -> false
            oldList[p0].text != newList[p1].text -> false
            else -> true
        }
    }
}