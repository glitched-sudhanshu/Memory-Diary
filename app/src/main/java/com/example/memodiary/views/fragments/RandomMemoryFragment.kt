package com.example.memodiary.views.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.memodiary.R
import com.example.memodiary.databinding.FragmentRandomMemoryBinding
import com.example.memodiary.viewmodels.NotificationsViewModel
import com.example.memodiary.viewmodels.RandomMemeViewModel

//TODO: This will not be a random memory in my application so change it accordingly, but for now I'm naming it as RandomMemoryFragment. names of string value, navigation me value, layout value
class RandomMemoryFragment : Fragment() {

    private var mBinding: FragmentRandomMemoryBinding? = null

    private lateinit var mRandomMemeViewModel : RandomMemeViewModel

    private var mProgressDialog : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentRandomMemoryBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomMemeViewModel = ViewModelProvider(this).get(RandomMemeViewModel::class.java)

        mRandomMemeViewModel.getRandomMemeFromAPI()

        randomMemeViewModelObserver()

        mBinding!!.srlRandomMeme.setOnRefreshListener {
            mRandomMemeViewModel.getRandomMemeFromAPI()
        }

    }

    private fun randomMemeViewModelObserver() {
        mRandomMemeViewModel.randomMemeResponse.observe(viewLifecycleOwner
        ) { randomMemeResponse ->
            randomMemeResponse?.let {
                Log.i("Random meme response", "$randomMemeResponse")
                if(mBinding!!.srlRandomMeme.isRefreshing){
                    mBinding!!.srlRandomMeme.isRefreshing = false
                }
                Glide.with(this)
                    .load(randomMemeResponse.url)
                    .into(mBinding!!.ivMemoryImage)
            }
        }

        mRandomMemeViewModel.randomMemeLoadingError.observe(viewLifecycleOwner
        ) { dataError ->
            dataError?.let {
                Log.e("Random meme api error", "$dataError")
                if(mBinding!!.srlRandomMeme.isRefreshing){
                    mBinding!!.srlRandomMeme.isRefreshing = false
                }
            }
        }
        mRandomMemeViewModel.loadRandomMeme.observe(viewLifecycleOwner
        ) { loadRandomMeme ->
            loadRandomMeme?.let {
                Log.e("Random meme loading", "$loadRandomMeme")

                if(loadRandomMeme && !mBinding!!.srlRandomMeme.isRefreshing){
                    showCustomProgressDialog()
                }else{
                    hideCustomProgressDialog()
                }
            }
        }
    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }
    private fun hideCustomProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}