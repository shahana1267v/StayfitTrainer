package com.bigOne.stayfittrainer.ui.meal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentSavemealListBinding
import com.bigOne.stayfittrainer.datas.model.SavedFood
import com.bigOne.stayfittrainer.utils.CommonUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MealLogListSaveFragment : Fragment() {

    lateinit var binding: FragmentSavemealListBinding
    private val mealViewModel: MealViewModel by activityViewModels()
    private lateinit var mAdapter: SelectedFoodAdapter
    private var mDialog: MealLogSaveFragmentDialog? = null
    var mealType = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_savemeal_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        init()
        observer()
    }

    private fun init() {
        binding.addFood.setOnClickListener {
            val action =
                MealLogListSaveFragmentDirections.actionMealLogListSaveFragmentToMealLogFragment(
                    mealType
                )
            findNavController().navigate(action);
        }
        binding.saveBtn.setOnClickListener {
            mealViewModel.saveFoodDb().observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    mealViewModel.clearSavedFood()
                    val action =
                        MealLogListSaveFragmentDirections.actionMealLogListSaveFragmentToHomeFragment()
                    findNavController().navigate(action);
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            mealViewModel.clearSavedFood()
        }

    }

    private fun initAdapter() {
        binding.apply {
            selctedFoodRecycleList.layoutManager = LinearLayoutManager(context)
            selctedFoodRecycleList.setHasFixedSize(true)
            mAdapter = SelectedFoodAdapter(mutableListOf(), requireContext())
            selctedFoodRecycleList.adapter = mAdapter
        }
        mAdapter.onitemClickListner = object : SelectedFoodAdapter.OnItemClickListener {
            override fun ondelete(item: SavedFood) {
                val context: Context = ContextThemeWrapper(requireContext(), R.style.DialogStyle)
                MaterialAlertDialogBuilder(context)
                    .setMessage(resources.getString(R.string.delete_it))
                    .setCancelable(false)
                    .setNeutralButton(resources.getString(R.string.general_no)) { _, _ ->
                    }
                    .setPositiveButton(resources.getString(R.string.general_yes)) { _, _ ->
                        mealViewModel.deletefood(item)
                    }.show()
            }
        }
    }


    private fun observer() {
        mealViewModel.savedFoods.observe(viewLifecycleOwner) { foods ->
            Log.e("saveFoods", foods.toString())
            if (foods.isNotEmpty()) {
                mealType = foods[0].mealType
                binding.toolbar.title = CommonUtils.getMealType(foods[0].mealType)
                binding.totalCalorieValue.text = getTotalCalorie(foods).toString()
                mAdapter.setItems(foods)
            }

        }

    }

    private fun getTotalCalorie(foods: List<SavedFood>): Int? {
        var totalCalorie = 0
        foods.forEach { food ->
            totalCalorie += (food.selectedQuantity.toDouble() * food.calorie).toInt()
        }
        return totalCalorie
    }
}


