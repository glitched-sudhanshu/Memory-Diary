package com.example.memodiary.views.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.memodiary.R
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.FragmentMemoryDetailBinding
import com.example.memodiary.models.entities.MemoDiary
import com.example.memodiary.utils.Constants
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class MemoryDetailFragment : Fragment() {

    private var mBinding: FragmentMemoryDetailBinding? = null

    private var mMemoDiaryDetails: MemoDiary? = null

    private val mFavMemoryViewModel: MemoDiaryViewModel by viewModels {
        MemoDiaryViewModelFactory(((requireActivity().application) as MemoDiaryApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        val args: MemoryDetailFragmentArgs by navArgs()
        //here memory details is the label of that fragment
        Log.i("Memory title", args.memoryDetails.title)

        mMemoDiaryDetails = args.memoryDetails


        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.memoryDetails.image)
                    .centerCrop()
                    //not adding palette
                    .into(mBinding!!.ivMemoryImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mBinding!!.tvTitle.text = it.memoryDetails.title.uppercase(Locale.ROOT)
            mBinding!!.tvType.text = it.memoryDetails.type.replaceFirstChar { it ->
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            mBinding!!.tvPeople.text = it.memoryDetails.peopleInvolved
            mBinding!!.tvDesc.text = it.memoryDetails.description
            mBinding!!.tvDate.text = it.memoryDetails.date
            mBinding!!.tvDiaryEntryTime.text =
                resources.getString(R.string.approximate_diary_entry_time, it.memoryDetails.time)

            //NOTE: Here we are making sure that the heart stays the same whether it was marked favourite or not
            if (args.memoryDetails.favouriteMemory) {
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite))
            } else {
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_unselected_favorite))

            }
        }

        //NOTE: Here we are marking the heart whether it is favourite or not while clicking it
        mBinding!!.ivFavMemory.setOnClickListener {
            //negating the favourite memory. If it was fav, then clicked, means not fav now and vice versa
            args.memoryDetails.favouriteMemory = !args.memoryDetails.favouriteMemory

            mFavMemoryViewModel.update(args.memoryDetails)

            if (args.memoryDetails.favouriteMemory) {
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite))
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_added_to_favourites),
                    Toast.LENGTH_SHORT).show()
            } else {
                mBinding!!.ivFavMemory.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_unselected_favorite))
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_removed_from_favourites),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                Log.i(TAG, "onMenuItemSelected: menu selected")
                val type = "text/plain"
                val subject = "Checkout this memory of us"
                var extraText = ""
                val shareWith = "Share with"

                mMemoDiaryDetails?.let {
                    var image = ""
                    if (it.imageSource == Constants.MEMO_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }
                    val description = Html.fromHtml(
                        it.description,
                        Html.FROM_HTML_MODE_COMPACT
                    ).toString()

                    extraText = "" +
                            "$image \n" +
                            "\n Title: ${it.title} \n\n Type: ${it.type} \n\n People involved: ${it.peopleInvolved} \n\n Date: ${it.date} \n\n Description: $description"

                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}


