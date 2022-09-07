package com.example.memodiary.views.fragments

import android.app.AlertDialog
import android.app.Dialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memodiary.R
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.DialogCustomListBinding
import com.example.memodiary.databinding.FragmentAllMemoryBinding
import com.example.memodiary.models.entities.MemoDiary
import com.example.memodiary.utils.Constants
import com.example.memodiary.viewmodels.HomeViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import com.example.memodiary.views.activities.AddUpdateMemoryActivity
import com.example.memodiary.views.activities.MainActivity
import com.example.memodiary.views.adapters.CustomListItemAdapter
import com.example.memodiary.views.adapters.MemoryDiaryAdapter

class AllMemoryFragment : Fragment() {

    private var mBinding: FragmentAllMemoryBinding? = null

    private lateinit var mMemoryDiaryAdapter: MemoryDiaryAdapter
    private lateinit var mCustomListDialog: Dialog

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

                    R.id.action_filter_memory -> filterMemoryListDialog()
                }
                return true
            }
        })

        return mBinding!!.root

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvMemoryList.layoutManager = GridLayoutManager(requireActivity(),2)

        mMemoryDiaryAdapter = MemoryDiaryAdapter(this)

        mBinding!!.rvMemoryList.adapter = mMemoryDiaryAdapter

        mMemoDiaryViewModel.allMemoryList.observe(viewLifecycleOwner){
            memories ->
            run {
                memories.let {
                    if(it.isNotEmpty()){
                        mBinding!!.rvMemoryList.visibility = View.VISIBLE
                        mBinding!!.tvNoMemoryAddedYet.visibility = View.GONE

                        mMemoryDiaryAdapter.memoryList(it)
                    }else{
                        mBinding!!.rvMemoryList.visibility = View.GONE
                        mBinding!!.tvNoMemoryAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    //function to move to the other fragment[fragment of each memory detail]
    fun memoryDetails(memoDiary: MemoDiary){
        //while navigating you have to pass the arguments(data you need to transfer)
        findNavController().navigate(AllMemoryFragmentDirections.actionNavigationAllMemoryToNavigationMemoryDetail(memoDiary))

//        here we are calling the function from the main activity which is not a part of it
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    fun deleteMemory(memoDiary: MemoDiary){
        //creating alert dialog "ARE YOU SURE?"
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_memory))
        builder.setMessage(resources.getString(R.string.msg_delete_memory, memoDiary.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes))
        {dialogInterface, _->
            mMemoDiaryViewModel.delete(memoDiary)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.lbl_no)){
            dialogInterface,_->dialogInterface.dismiss()
        }
        val alertDialog : AlertDialog = builder.create()
        //user has to select something
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterMemoryListDialog(){
        mCustomListDialog = Dialog(requireActivity())
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
//means that this binding is going to be complete dialog
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val peopleInvolved = Constants.peopleInvolvedInMemory()
        peopleInvolved.add(0, Constants.ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(requireActivity(), this@AllMemoryFragment, peopleInvolved, Constants.FILTER_SELECTION )

        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    //using the function in AllMemoryFragment in the adapter in its onClickListener
    fun filterSelection(filterItemSelection : String){
        //dismiss the dialog when something is selected
        mCustomListDialog.dismiss()
        Log.i("Filter selection", filterItemSelection)
        if(filterItemSelection == Constants.ALL_ITEMS){
            mMemoDiaryViewModel.allMemoryList.observe(viewLifecycleOwner){
                    memories ->
                run {
                    memories.let {
                        if(it.isNotEmpty()){
                            mBinding!!.rvMemoryList.visibility = View.VISIBLE
                            mBinding!!.tvNoMemoryAddedYet.visibility = View.GONE

                            mMemoryDiaryAdapter.memoryList(it)
                        }else{
                            mBinding!!.rvMemoryList.visibility = View.GONE
                            mBinding!!.tvNoMemoryAddedYet.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }else{
            mMemoDiaryViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner){
                memory -> {
                    memory.let {
                        if(it.isNotEmpty()){
                            mBinding!!.rvMemoryList.visibility = View.VISIBLE
                            mBinding!!.tvNoMemoryAddedYet.visibility = View.GONE
                            mMemoryDiaryAdapter.memoryList(it)
                        }else{
                            mBinding!!.rvMemoryList.visibility = View.GONE
                            mBinding!!.tvNoMemoryAddedYet.visibility = View.VISIBLE
                        }
                    }
            }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}