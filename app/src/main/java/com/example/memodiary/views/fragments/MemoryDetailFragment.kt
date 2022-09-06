package com.example.memodiary.views.fragments

import android.content.ContentValues.TAG
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memodiary.R
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.FragmentAllMemoryBinding
import com.example.memodiary.databinding.FragmentMemoryDetailBinding
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import java.io.IOException
import java.util.*


class MemoryDetailFragment : Fragment() {

    private var mBinding : FragmentMemoryDetailBinding? = null

    private val mFavMemoryViewModel : MemoDiaryViewModel by viewModels {
        MemoDiaryViewModelFactory(((requireActivity().application) as MemoDiaryApplication).repository)
    }

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
                        //not adding palette
                    .into(mBinding!!.ivMemoryImage)
            }catch (e:IOException){
                e.printStackTrace()
            }

            mBinding!!.tvTitle.text = it.memoryDetails.title.uppercase(Locale.ROOT)
            mBinding!!.tvType.text = it.memoryDetails.type.replaceFirstChar { it ->
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            mBinding!!.tvPeople.text = it.memoryDetails.peopleInvolved
            mBinding!!.tvDesc.text = it.memoryDetails.description
            mBinding!!.tvDate.text = it.memoryDetails.date
            mBinding!!.tvDiaryEntryTime.text = resources.getString(R.string.approximate_diary_entry_time, it.memoryDetails.time)

            //NOTE: Here we are making sure that the heart stays the same whether it was marked favourite or not
            if(args.memoryDetails.favouriteMemory){
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite))
            }else{
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_unselected_favorite))

            }
        }

        //NOTE: Here we are marking the heart whether it is favourite or not while clicking it
        mBinding!!.ivFavMemory.setOnClickListener{
            //negating the favourite memory. If it was fav, then clicked, means not fav now and vice versa
            args.memoryDetails.favouriteMemory = !args.memoryDetails.favouriteMemory

            mFavMemoryViewModel.update(args.memoryDetails)

            if(args.memoryDetails.favouriteMemory){
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite))
                Toast.makeText(requireActivity(), resources.getString(R.string.msg_added_to_favourites), Toast.LENGTH_SHORT).show()
            }else{
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_unselected_favorite))
                Toast.makeText(requireActivity(), resources.getString(R.string.msg_removed_from_favourites), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}