package com.bigOne.stayfittrainer.ui.meal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentMeallogsaveBinding
import com.bigOne.stayfittrainer.datas.model.FoodDetails
import com.bigOne.stayfittrainer.datas.model.Serving
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MealLogSaveFragmentDialog(private val id: String, private val mealType: Int) : BottomSheetDialogFragment() {

    lateinit var binding:FragmentMeallogsaveBinding
    private  val  mealViewModel: MealViewModel by activityViewModels()
   private  var servingDescriptions:MutableList<String>? = mutableListOf()
    private  var  selectedServingDescription: String? =null
    private  var servingData: Serving? =null
    private  var   foodDetails: FoodDetails? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meallogsave, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()
        Log.e("selectedItemInSecondPage",id)
    }



    private fun observer() {
        mealViewModel.getMealDetails(id).observe(viewLifecycleOwner){
            binding.apply {
                foodName.text = it?.food_name
                foodDetails = it
                getSelectUnit(it)
            }
        }
        mealViewModel.dismissDialog.observe(viewLifecycleOwner){
            if(it){
                dismiss()
            }
        }
    }

    private fun getSelectUnit(foodDetails: FoodDetails?) {
         servingDescriptions = foodDetails?.servings?.serving?.map { it.serving_description } as MutableList<String>?
         selectedServingDescription= servingDescriptions?.get(0)
        servingData = foodDetails?.servings?.serving?.find { it.serving_description == selectedServingDescription }
        servingDescriptions?.let { descriptions ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, descriptions)
            binding.serveList.setAdapter(adapter)
            // Set the first value automatically
            if (descriptions.isNotEmpty()) {
                binding.serveList.setText(descriptions[0], false)
            }
            binding.calorieTextValue.text = servingData?.calories.toString()
        }

        }

    private fun init() {
        binding.quantityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                val quantityString = s.toString()
                if (quantityString.isNotEmpty()) {
                    val quantity = quantityString.toDouble() // Convert string to double
                    updateCalories(quantity)
                }
            }
        })

        binding.serveList.setOnItemClickListener { parent, view, position, id ->
            val selectedServingDescription = parent.getItemAtPosition(position) as String
             servingData = foodDetails?.servings?.serving?.find { it.serving_description == selectedServingDescription }
            binding.quantityEditText.setText("1")
            updateCalories(1.0)
        }
        binding.saveBtn.setOnClickListener {
            if (servingData != null && selectedServingDescription != null) {
              mealViewModel.saveFood(binding.quantityEditText.text.toString(), servingData!!, selectedServingDescription!!, mealType, foodDetails!!)
                val action = MealLogFragmentDirections.actionMealLogFragmentToMealLogListSaveFragment()
                findNavController().navigate(action);
                dismiss()
            }
        }
    }

    private fun updateCalories(quantity: Double) {
       val sumCalorie = quantity * servingData?.calories?.toDouble()!!
        binding.apply {
            calorieTextValue.text = sumCalorie.toString()
        }
    }

}