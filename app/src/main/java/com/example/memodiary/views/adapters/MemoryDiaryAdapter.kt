package com.example.memodiary.views.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memodiary.R
import com.example.memodiary.databinding.FragmentAllMemoryBinding
import com.example.memodiary.databinding.ItemCustomListBinding
import com.example.memodiary.databinding.ItemMemoryLayoutBinding
import com.example.memodiary.models.entities.MemoDiary
import com.example.memodiary.views.fragments.AllMemoryFragment
import com.example.memodiary.views.fragments.FavouriteMemoryFragment

class MemoryDiaryAdapter(private val fragment: Fragment) : RecyclerView.Adapter<MemoryDiaryAdapter.ViewHolder>() {

    private var memories : List<MemoDiary> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMemoryLayoutBinding = ItemMemoryLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memory = memories[position]
        Glide.with(fragment)
            .load(memory.image)
            .into(holder.ivMemoryImage)
        holder.tvTitle.text = memory.title

        holder.itemView.setOnClickListener{
            //to move to the other fragment[fragment of each memory detail]
            if(fragment is AllMemoryFragment)fragment.memoryDetails(memory)
            if(fragment is FavouriteMemoryFragment)fragment.memoryDetails(memory)
        }

        holder.ibMore.setOnClickListener{
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if(it.itemId == R.id.action_edit_memory){
                    Log.i("You have clicked on", "Edit option of ${memory.id}")
                }else if(it.itemId == R.id.action_delete_memory){
                    Log.i("You have clicked on", "Edit option of ${memory.id}")
                }
                true
            }
            popup.show()
        }

        if(fragment is AllMemoryFragment)holder.ibMore.visibility = View.VISIBLE
        if(fragment is FavouriteMemoryFragment)holder.ibMore.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return memories.size
    }

    class ViewHolder(view : ItemMemoryLayoutBinding) : RecyclerView.ViewHolder(view.root){
        val tvTitle = view.tvMemoryTitle
        val ivMemoryImage = view.ivMemoryImage
        val ibMore = view.ibMore
    }

    fun memoryList(list : List<MemoDiary>){
        memories = list
        notifyDataSetChanged()
    }
}