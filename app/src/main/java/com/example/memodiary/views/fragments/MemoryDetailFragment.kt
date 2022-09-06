package com.example.memodiary.views.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.memodiary.R
import com.example.memodiary.databinding.FragmentAllMemoryBinding
import com.example.memodiary.databinding.FragmentMemoryDetailBinding
import java.io.IOException
import java.util.*


class MemoryDetailFragment : Fragment() {

    private var mBinding : FragmentMemoryDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentMemoryDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : MemoryDetailFragmentArgs by navArgs()
        //here memory details is the label of that fragment
        Log.i("Memory title", args.memoryDetails.title)
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.memoryDetails.image)
                    .centerCrop()
                    .into(mBinding!!.ivMemoryImage)
            }catch (e:IOException){
                e.printStackTrace()
            }

            mBinding!!.tvTitle.text = it.memoryDetails.title
            mBinding!!.tvType.text = it.memoryDetails.type.replaceFirstChar { it ->
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            mBinding!!.tvPeople.text = it.memoryDetails.peopleInvolved
            mBinding!!.tvDesc.text = it.memoryDetails.description
            mBinding!!.tvDiaryEntryTime.text = resources.getString(R.string.approximate_diary_entry_time, it.memoryDetails.time)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}