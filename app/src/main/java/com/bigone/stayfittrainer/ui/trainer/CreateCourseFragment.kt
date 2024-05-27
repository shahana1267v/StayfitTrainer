package com.bigOne.StayFitTrainer.ui.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.StayFitTrainer.R
import com.bigOne.StayFitTrainer.databinding.FragmentCreateCourseBinding

class CreateCourseFragment : Fragment() {
    lateinit var binding: FragmentCreateCourseBinding
    private lateinit var mAdapter: WorkOutAdapter
    private  val  trainerViewModel: TrainerViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var mDialog: WorkoutDialogFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_course, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        init()
        initAdapter()
    }
    private fun observe() {
        trainerViewModel.workouts.observe(viewLifecycleOwner){
            mAdapter.setItems(it)
        }
        trainerViewModel.isSaveCourse.observe(viewLifecycleOwner){
            if(it==true){
                Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                trainerViewModel.closeSave()
            }
        }
    }

    private fun init() {
        binding.apply {
            addWorkout.setOnClickListener {
                if (mDialog == null) mDialog = WorkoutDialogFragment()
                mDialog!!.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
                mDialog?.show(requireActivity().supportFragmentManager, WorkoutDialogFragment.TAG)
            }
            saveWorkOut.setOnClickListener {
                binding.apply {
                    trainerViewModel.saveCourse(courseNameText.text.toString(),timeText.text.toString(),diffText.text.toString(),
                        textDescription.text.toString())
                }

            }
        }
    }
    private fun initAdapter() {
        binding.apply {
            workoutList.layoutManager = LinearLayoutManager(context)
            workoutList.setHasFixedSize(true)
            mAdapter = WorkOutAdapter(mutableListOf(),requireContext())
            workoutList.adapter = mAdapter
        }

    }
}