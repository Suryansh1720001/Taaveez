package com.itssuryansh.taaveez.Tools

import androidx.recyclerview.widget.DiffUtil
import com.itssuryansh.taaveez.NotesEntity

class RecyclerViewDiffUtil(private val newList: ArrayList<NotesEntity>
    ,private val oldList:ArrayList<NotesEntity>)
    : DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
       return  newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode()==newList[newItemPosition].hashCode()
    }
}