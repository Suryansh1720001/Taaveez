package com.itssuryansh.taaveez.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itssuryansh.taaveez.TaaveezEntity
import com.itssuryansh.taaveez.databinding.ItemContentBinding
import kotlin.collections.ArrayList


class itemAdapter(private var items: ArrayList<TaaveezEntity>,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
                  private val ShareListener:(id:Int)->Unit,
                  private var mdetailedViewMode: Boolean // Add view mode parameter

)
    :RecyclerView.Adapter<itemAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemContentBinding): RecyclerView.ViewHolder(binding.root){


        val CVMain = binding.CVMain
        val tvTopic = binding.tvtextTopic
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
        val llTopic = binding.llTopic
        val  ivShare= binding.ivShare
        val tvDate = binding.tvDate
        val tvSmallDes = binding.tvSmallDes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(ItemContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvTopic.text = item.Topic
        holder.tvDate.text = item.Date

        if(mdetailedViewMode==false){
            holder.tvSmallDes.visibility = View.GONE

        }else{
            holder.tvSmallDes.visibility = View.VISIBLE

        }

        holder.tvSmallDes.text =  Html.fromHtml(item.SmallDes)

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
       return items.size
    }




    // Add this method to update the data and view mode
    // Add this method to update the data and view mode
    // Add this method to update the data and view mode
    fun updateData(newItems: ArrayList<TaaveezEntity>, newDetailedViewMode: Boolean) {
        items = newItems
        mdetailedViewMode = newDetailedViewMode
        notifyDataSetChanged()
    }


}