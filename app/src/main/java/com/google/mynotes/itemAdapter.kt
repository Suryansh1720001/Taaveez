package com.google.mynotes


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.mynotes.databinding.ItemPoemBinding


class itemAdapter(private val items: ArrayList<NotesEntity>,
                   private val updateListener:(id:Int)->Unit,
                   private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
                  private val ShareListener:(id:Int)->Unit,
)
    :RecyclerView.Adapter<itemAdapter.ViewHolder>() {


    class ViewHolder(binding:ItemPoemBinding): RecyclerView.ViewHolder(binding.root){
        val CVMain = binding.CVMain
        val tvTopic = binding.tvtextTopic
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
        val llTopic = binding.llTopic
        val  ivShare= binding.ivShare
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemPoemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvTopic.text = item.Topic
//        holder.tvEmail.text = item.email




//
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


}