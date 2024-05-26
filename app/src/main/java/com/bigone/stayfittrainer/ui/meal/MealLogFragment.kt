package com.bigOne.stayfittrainer.ui.meal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.FoodFactResponse
import com.bigOne.stayfittrainer.utils.CommonUtils.getMealType


class MealLogFragment : Fragment() {
    private val args: MealLogFragmentArgs by navArgs()
    private val mealViewModel: MealViewModel by activityViewModels()
    var mealType=0

    lateinit var binding: com.bigOne.stayfittrainer.databinding.FragmentMeallogBinding
    private lateinit var mAdapter: FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meallog, container, false)
        return binding.root
    }

    init {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mealType =args.mealType
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
        observer()
    }
    private var mDialog: MealLogSaveFragmentDialog? = null

    private fun initAdapter() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            mAdapter = FoodAdapter(mutableListOf(),requireContext())
            recyclerView.adapter = mAdapter
        }
        mAdapter.onitemClickListner = object : FoodAdapter.OnItemClickListener {
            override fun onClick(item: FoodFactResponse) {
                mAdapter.setItems(mutableListOf())
                Log.e("selectedItemInFirstPage",item.id)
                mDialog = MealLogSaveFragmentDialog(item.id,mealType)
                mDialog!!.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
                mDialog?.show(requireActivity().supportFragmentManager, "SaveMeal")
                mealViewModel.dismissDialog.observe(viewLifecycleOwner){
                    if(it) {
                        /*val action = MealLogFragmentDirections.actionMealLogFragmentToMealLogListSaveFragment()
                        findNavController().navigate(action);*/
                        mealViewModel.closeDialog()
                    }
                }

            }

        }
    }

    private fun observer() {
        mealViewModel.getalreadyMealDetails(args.mealType)
       mealViewModel.dismissDialog.observe(viewLifecycleOwner){
           if(it) {
               Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
              // mealViewModel.closeDialog()
           }
           }
       }

    private fun init() {
        binding.toolbar.title = getMealType(mealType)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null && query.length>2) {
                    mAdapter.setItems(mutableListOf())
                    mealViewModel.searchMeal(query).observe(viewLifecycleOwner) {
                        if (it != null) {
                            Log.e("Food", it.toString())
                          if (it.totalResult > 0) {
                                if (it.foods.isNotEmpty())
                                    mAdapter.setItems(it.foods)
                            }
                          else {
                                mAdapter.setItems(mutableListOf())
                            }
                        }
                    }
                }else{
                    mAdapter.setItems(mutableListOf())
                }
               return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        val closeBtn: View =  binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeBtn.setOnClickListener {
            binding.searchView.setQuery("", false) // reset Query text to be empty without submition
            binding.searchView.isIconified = true // Replace the x icon with the search icon
            mAdapter.setItems(mutableListOf())
        }

        binding.searchView.setOnCloseListener {
            // Handle close icon click here
            Log.e("query Adapter","Close click")
            mAdapter.setItems(mutableListOf()) // Clear the adapter when the close icon is clicked
            true // Return true to consume the event
        }
    }







}