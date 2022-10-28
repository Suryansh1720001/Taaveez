package com.google.mynotes

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.mynotes.databinding.ItemPoemBinding
import kotlinx.coroutines.launch

class itemAdapter(private val items: ArrayList<NotesEntity>,
                   private val updateListener:(id:Int)->Unit,
                   private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
)
    :RecyclerView.Adapter<itemAdapter.ViewHolder>() {


    class ViewHolder(binding:ItemPoemBinding): RecyclerView.ViewHolder(binding.root){
        val CVMain = binding.CVMain
        val tvTopic = binding.tvtextTopic
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
        val llTopic = binding.llTopic
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
    }

    override fun getItemCount(): Int {
       return items.size
    }


}