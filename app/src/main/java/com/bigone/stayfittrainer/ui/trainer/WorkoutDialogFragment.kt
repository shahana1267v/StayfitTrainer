package com.bigOne.StayFitTrainer.ui.trainer

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bigOne.StayFitTrainer.R
import com.bigOne.StayFitTrainer.databinding.FragmentWorkoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class WorkoutDialogFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentWorkoutBinding
    private  val  trainerViewModel: TrainerViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it -> val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun init(){
        binding.apply {
            applyWorkOut.setOnClickListener {
                trainerViewModel.saveWorkOut(
                    workoutNameText.text.toString(), timeText.text.toString(),
                    diffText.text.toString(),
                    urlText.text.toString(),calorieText.text.toString()
                ).observe(viewLifecycleOwner){
                    Toast.makeText(requireContext(), "saved successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }


    companion object {
        const val TAG = "WDialogSheet"

    }

}