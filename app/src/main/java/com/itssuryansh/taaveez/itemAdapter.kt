package com.itssuryansh.taaveez

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itssuryansh.taaveez.databinding.ItemPoemBinding


class itemAdapter(
    /*private var oldItems: ArrayList<NotesEntity>,*/
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
                  private val ShareListener:(id:Int)->Unit,
)
    :ListAdapter<NotesEntity,itemAdapter.ViewHolder>(DiffUtil()) {


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
        val item = getItem(position)

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


   class DiffUtil : ItemCallback<NotesEntity>(){
       override fun areItemsTheSame(oldItem: NotesEntity, newItem: NotesEntity): Boolean {
           return oldItem.id == newItem.id
       }

       override fun areContentsTheSame(oldItem: NotesEntity, newItem: NotesEntity): Boolean {
            return when{
                oldItem.id != newItem.id ->{ false }
                oldItem.CreatedDate != newItem.CreatedDate -> {false}
                oldItem.Poem != newItem.Poem -> {false}
                oldItem.Topic != newItem.Topic -> {false}
                oldItem.Date != newItem.Date -> {false}
                else->true
            }
       }

   }
}