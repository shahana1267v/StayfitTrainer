package com.bigOne.stayfittrainer.ui.bmi_calculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentAddWeightBinding
import com.bigOne.stayfittrainer.ui.home.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddWeightFragment : BottomSheetDialogFragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentAddWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_weight, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    private fun observe() {
        mainViewModel.isUpdateUser.observe(viewLifecycleOwner) {
            if(it) {
                mainViewModel.clearUpdateUser()
                binding.weightEditText.text=null
                Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()
                dismiss()
            }

        }
    }

    private fun init() {
        binding.updateBtn.setOnClickListener {
            if (binding.weightEditText.text?.isEmpty() == true) {
                binding.addweightField.error = "Required"
            } else {
                try {
                    mainViewModel.UpdateWeight(binding.weightEditText.text.toString())
                } catch (e: Exception) {
                    Log.e("DocumentSnapshot", e.toString())
                }

            }
        }

    }

    companion object {

        const val TAG = "WeightSheet"


    }
}






