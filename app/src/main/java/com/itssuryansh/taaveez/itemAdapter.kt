package com.itssuryansh.taaveez

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itssuryansh.taaveez.Tools.RecyclerViewDiffUtil
import com.itssuryansh.taaveez.databinding.ItemPoemBinding
import kotlin.collections.ArrayList


class itemAdapter(private var oldItems: ArrayList<NotesEntity>,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
                  private val ShareListener:(id:Int)->Unit,
)
    :RecyclerView.Adapter<itemAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemPoemBinding): RecyclerView.ViewHolder(binding.root){
        val CVMain = binding.CVMain
        val tvTopic = binding.tvtextTopic
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
        val llTopic = binding.llTopic
        val  ivShare= binding.ivShare
        val tvDate = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemPoemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val context = holder.itemView.context
        val item = oldItems[position]

        holder.tvTopic.text = item.Topic
        holder.tvDate.text = item.Date

        holder.ivEdit.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener{
            deleteListener.invoke(item.id)
        }
        holder.llTopic.setOnClickListener{
            OpenListener.invoke(item.id)
        }
        holder.ivShare.setOnClickListener{
            ShareListener.invoke(item.id)
        }



    }

    override fun getItemCount(): Int {
       return oldItems.size
    }

    fun initiateDiffUtilCallback(newItems:ArrayList<NotesEntity>){
        val diffUtilInstance  = RecyclerViewDiffUtil(newItems,oldItems)

        val diffResult = DiffUtil.calculateDiff(diffUtilInstance)
        val start = System.currentTimeMillis()
        oldItems = newItems
        diffResult.dispatchUpdatesTo(this)
        val end = System.currentTimeMillis()
        Log.d("time", "initiateDiffUtilCallback- update time : ${(end-start)}ms")
    }
}