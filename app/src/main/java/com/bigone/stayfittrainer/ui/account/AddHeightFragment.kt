package com.bigOne.StayFitTrainer.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bigOne.StayFitTrainer.R
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bigOne.StayFitTrainer.databinding.FragmentAddHeightBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddHeightFragment : BottomSheetDialogFragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var binding: FragmentAddHeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_height, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    private fun observe() {

            accountViewModel.isUpdateUser.observe(viewLifecycleOwner) {
                if(it) {
                    accountViewModel.clearUpdateUser()
                    binding.heightEditText.text=null
                    Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()
                    dismiss()
                }

            }
        }




    private fun init() {
        binding.updateBtn.setOnClickListener {
            if (binding.heightEditText.text?.isEmpty() == true) {
                binding.addheightField.error = "Required"
            } else {
                try {
                    accountViewModel.UpdateHeight(binding.heightEditText.text.toString())
                } catch (e: Exception) {
                    Log.e("DocumentSnapshot", e.toString())
                }

            }
        }

    }


    companion object {

        const val TAG = "HeightSheet"


    }
}
