package com.example.memodiary.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.FragmentFavouriteMemoryBinding
import com.example.memodiary.models.entities.MemoDiary
import com.example.memodiary.viewmodels.DashboardViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import com.example.memodiary.views.activities.MainActivity
import com.example.memodiary.views.adapters.MemoryDiaryAdapter

class FavouriteMemoryFragment : Fragment() {

    private var mBinding: FragmentFavouriteMemoryBinding? = null

    private val mFavMemoViewModel : MemoDiaryViewModel by viewModels{
        MemoDiaryViewModelFactory(((requireActivity().application) as MemoDiaryApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentFavouriteMemoryBinding.inflate(inflater, container, false)


        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvMemoryList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favMemoryAdapter = MemoryDiaryAdapter(this)
        mBinding!!.rvMemoryList.adapter = favMemoryAdapter

        //observe the data change using the viewModel
        mFavMemoViewModel.allFavouriteMemory.observe(viewLifecycleOwner){
            memories ->
            memories.let {
                if(it.isNotEmpty()){
                    for(memory in it){
                        Log.i("Favourite memory", "${memory.id}")
                    }
                    mBinding!!.rvMemoryList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavMemoryAddedYet.visibility = View.GONE
                    favMemoryAdapter.memoryList(it)
                }else{
                    Log.i("Favourite memory", "No favourite memory")
                    mBinding!!.rvMemoryList.visibility = View.GONE
                    mBinding!!.tvNoFavMemoryAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    //function to move to the other fragment[fragment of each memory detail]
    fun memoryDetails(memoDiary: MemoDiary){
        //while navigating you have to pass the arguments(data you need to transfer)
        findNavController().navigate(FavouriteMemoryFragmentDirections.actionNavigationFavouriteMemoryToNavigationMemoryDetail(memoDiary))

//        here we are calling the function from the main activity which is not a part of it
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}