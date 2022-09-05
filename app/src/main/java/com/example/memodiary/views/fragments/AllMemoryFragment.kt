package com.example.memodiary.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memodiary.R
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.FragmentAllMemoryBinding
import com.example.memodiary.viewmodels.HomeViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import com.example.memodiary.views.activities.AddUpdateMemoryActivity
import com.example.memodiary.views.adapters.MemoryDiaryAdapter

class AllMemoryFragment : Fragment() {

    private var mBinding: FragmentAllMemoryBinding? = null

    private val mMemoDiaryViewModel: MemoDiaryViewModel by viewModels {
        MemoDiaryViewModelFactory((requireActivity().application as MemoDiaryApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentAllMemoryBinding.inflate(inflater, container,false)

    //16:55
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(!menu.hasVisibleItems())
                    menuInflater.inflate(R.menu.menu_all_memory, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId)
                {
                    R.id.action_add_memory -> startActivity(Intent(requireActivity(), AddUpdateMemoryActivity::class.java))
                }
                return true
            }
        })

        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvMemoryList.layoutManager = GridLayoutManager(requireActivity(),2)
        val favMemoryAdapter = MemoryDiaryAdapter(this@AllMemoryFragment)

        mBinding!!.rvMemoryList.adapter = favMemoryAdapter

        mMemoDiaryViewModel.allMemoryList.observe(viewLifecycleOwner){
            memories ->
            run {
                memories.let {
                    if(it.isNotEmpty()){
                        mBinding!!.rvMemoryList.visibility = View.VISIBLE
                        mBinding!!.tvNoMemoryAddedYet.visibility = View.GONE

                        favMemoryAdapter.memoryList(it)
                    }else{
                        mBinding!!.rvMemoryList.visibility = View.GONE
                        mBinding!!.tvNoMemoryAddedYet.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}