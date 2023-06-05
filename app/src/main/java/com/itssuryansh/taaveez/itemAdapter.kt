package com.itssuryansh.taaveez

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itssuryansh.taaveez.databinding.ItemPoemBinding
import kotlin.collections.ArrayList


class itemAdapter(private val items: ArrayList<NotesEntity>,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit,
                  private val OpenListener:(id:Int)->Unit,
                  private val ShareListener:(id:Int)->Unit,
)
    :RecyclerView.Adapter<itemAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemPoemBinding): RecyclerView.ViewHolder(binding.root){

        val sideBar=binding.ivSideBar
        //this is for changing the color of it with respect to the position
        val tvTopic = binding.tvTitle
        //Todo-assign it to the tv_title
        val ivEdit = binding.ibEdit
        //Todo-assign it to the edit image view

        val ivDelete = binding.ibDelete
        //Todo-assign it to the delete image view
        val cvMain = binding.cvMain
        //Todo-assign it to the cvMAin
        val  ivShare= binding.ibShare
        //todo -assign it to the share image view
        val tvContent=binding.tvContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemPoemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val context = holder.itemView.context
        val item = items[position]



        val poemSize=item.Poem.length

        val contentMiniString:String=item.Poem.substring(0,(poemSize/2).toInt()) +"...."

        holder.tvTopic.text=item.Topic

        holder.tvContent.text=contentMiniString

        holder.ivEdit?.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete?.setOnClickListener{
            deleteListener.invoke(item.id)
        }
        holder.cvMain.setOnClickListener{
            OpenListener.invoke(item.id)
        }
        holder.ivShare?.setOnClickListener{
            ShareListener.invoke(item.id)
        }

        if(position%2==0)
        {
            holder.sideBar.setBackgroundColor(ContextCompat.getColor(context,R.color.lightPink))
        }
        else
        {
            holder.sideBar.setBackgroundColor(ContextCompat.getColor(context,R.color.navyBluelight))
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
}