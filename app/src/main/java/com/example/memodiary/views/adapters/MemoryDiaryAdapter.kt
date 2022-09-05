package com.example.memodiary.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memodiary.R
import com.example.memodiary.databinding.ItemCustomListBinding
import com.example.memodiary.databinding.ItemMemoryLayoutBinding
import com.example.memodiary.models.entities.MemoDiary

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
    }

    override fun getItemCount(): Int {
        return memories.size
    }

    class ViewHolder(view : ItemMemoryLayoutBinding) : RecyclerView.ViewHolder(view.root){
        val tvTitle = view.tvMemoryTitle
        val ivMemoryImage = view.ivMemoryImage
    }

    fun memoryList(list : List<MemoDiary>){
        memories = list
        notifyDataSetChanged()
    }
}