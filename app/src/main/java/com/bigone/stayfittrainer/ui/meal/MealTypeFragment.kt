package com.bigOne.stayfittrainer.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentMealTypeBinding
import com.bigOne.stayfittrainer.ui.home.HomeFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MealTypeFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentMealTypeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_type, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.radiogroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.breakfast -> {
                    clearselectedData()
                    val action = HomeFragmentDirections.actionHomeFragmentToMealLogFragment(BF)
                    findNavController().navigate(action);

                }
                R.id.lunch -> {
                    clearselectedData()
                    val action = HomeFragmentDirections.actionHomeFragmentToMealLogFragment(LH)
                    findNavController().navigate(action);

                }
                R.id.dinner -> {
                    clearselectedData()
                    val action = HomeFragmentDirections.actionHomeFragmentToMealLogFragment(DN)
                    findNavController().navigate(action);

                }
                R.id.mrngSnack -> {
                    clearselectedData()
                    val action = HomeFragmentDirections.actionHomeFragmentToMealLogFragment(MS)
                    findNavController().navigate(action);
                }
                R.id.evngSnack -> {
                    clearselectedData()
                    val action = HomeFragmentDirections.actionHomeFragmentToMealLogFragment(ES)
                    findNavController().navigate(action);
                }
            }
        }
    }

    private fun clearselectedData() {
        binding.radiogroup.clearCheck()
        dismiss()
    }

    companion object {
        const val TAG = "DialogSheet"
        const val  BF =1
        const val  MS =2
        const val  LH =3
        const val  ES =4
        const val  DN =5
    }
    override fun onResume() {
        super.onResume()
        // Clear the checked state of the RadioGroup
        binding.radiogroup.clearCheck()
    }
}