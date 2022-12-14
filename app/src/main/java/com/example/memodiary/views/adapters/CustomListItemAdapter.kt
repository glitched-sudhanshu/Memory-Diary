package com.example.memodiary.views.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.memodiary.databinding.ItemCustomListBinding
import com.example.memodiary.views.activities.AddUpdateMemoryActivity
import com.example.memodiary.views.fragments.AllMemoryFragment

class CustomListItemAdapter(
    private val activity: Activity,
    //making it optional to pass
    private val fragment : Fragment?,
    private val listItems: List<String>,
    private val selection : String
) :RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemCustomListBinding = ItemCustomListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    //how each item will look like in the adapter
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item

        holder.itemView.setOnClickListener {
            if (activity is AddUpdateMemoryActivity) {
                activity.selectedListItem(item, selection)
            }
            //fragment check because if used from another fragment, app may crash
            if(fragment is AllMemoryFragment){
                //using the function in AllMemoryFragment
                fragment.filterSelection(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    //to access elements of the item layout view
    class ViewHolder(view : ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }

}