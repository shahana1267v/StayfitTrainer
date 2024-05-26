package com.bigOne.stayfittrainer.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentCoursesBinding
import com.bigOne.stayfittrainer.datas.model.CourseData
import com.bigOne.stayfittrainer.ui.mycourse.MyCourseViewModel
import com.bigOne.stayfittrainer.ui.trainer.WorkoutDialogFragment

class CoursesFragment : Fragment() {
    lateinit var binding: FragmentCoursesBinding
    private lateinit var mAdapter: CourseAdapter
    private  val  myCourseViewModel: MyCourseViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var mDialog: WorkoutDialogFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_courses, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        init()
        initAdapter()
    }
    private fun observe() {
        myCourseViewModel.getMyCourses().observe(viewLifecycleOwner){
            mAdapter.setItems(it)
        }
    }

    private fun init() {

    }
    private fun initAdapter() {
        binding.apply {
            courseList.layoutManager = LinearLayoutManager(context)
            courseList.setHasFixedSize(true)
            mAdapter = CourseAdapter(mutableListOf(),requireContext())
            courseList.adapter = mAdapter
        }

        mAdapter.onitemClickListner = object : CourseAdapter.OnItemClickListener {
            override fun onClick(item: CourseData) {
                val action = CoursesFragmentDirections.actionCoursesFragmentToWorkoutCourseFragment(item.workouts.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }
}

