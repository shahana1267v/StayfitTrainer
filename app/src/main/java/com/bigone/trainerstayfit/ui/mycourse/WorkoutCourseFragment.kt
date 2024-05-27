package com.bigOne.trainerstayfit.ui.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.trainerstayfit.R
import com.bigOne.trainerstayfit.databinding.FragmentWorkoutCourseBinding
import com.bigOne.trainerstayfit.datas.model.WorkoutData


class WorkoutCourseFragment : Fragment() {

    private val args: WorkoutCourseFragmentArgs by navArgs()

    lateinit var binding: FragmentWorkoutCourseBinding
    private lateinit var mAdapter: WorkOutCourseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_course, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        init()
        val workouts = args.workouts?.toList()
        initAdapter(workouts)
    }
    private fun observe() {

    }

    private fun init() {
        binding.toolbar.setOnClickListener {
                findNavController().popBackStack()
        }
    }
    private fun initAdapter(workouts: List<WorkoutData>?) {
        binding.apply {
            workoutList.layoutManager = LinearLayoutManager(context)
            workoutList.setHasFixedSize(true)
            mAdapter = WorkOutCourseAdapter(mutableListOf(), requireContext())
            workoutList.adapter = mAdapter
            if (workouts != null) {
                mAdapter.setItems(workouts)
            }

            mAdapter.onitemClickListner = object : WorkOutCourseAdapter.OnItemClickListener {
                override fun onClick(item: WorkoutData) {
                    val action = WorkoutCourseFragmentDirections.actionWorkoutCourseFragmentToWorkoutDetailsFragment(item)
                    findNavController().navigate(action)
                }
            }
        }
    }

    companion object {

    }
}