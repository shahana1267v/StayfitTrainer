package com.bigOne.StayFitTrainer.ui.trainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.StayFitTrainer.R
import com.bigOne.StayFitTrainer.databinding.FragmentMyCourseBinding
import com.bigOne.StayFitTrainer.datas.model.CourseData
import com.bigOne.StayFitTrainer.ui.mycourse.MyCourseAdapter
import com.bigOne.StayFitTrainer.ui.mycourse.MyCourseViewModel
import com.google.firebase.auth.FirebaseAuth

class MyCourseFragment : Fragment() {
    lateinit var binding: FragmentMyCourseBinding
    private lateinit var mAdapter: MyCourseAdapter
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_course, container, false)
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
          Log.e("Courses My",it.toString())
          val user = FirebaseAuth.getInstance().currentUser
          val myCourses = it.filter { it-> it.trainerId == user!!.uid }
          mAdapter.setItems(myCourses)
      }
    }

    private fun init() {

    }
    private fun initAdapter() {
        binding.apply {
            myCourseList.layoutManager = LinearLayoutManager(context)
            myCourseList.setHasFixedSize(true)
            mAdapter = MyCourseAdapter(mutableListOf(),requireContext())
            myCourseList.adapter = mAdapter
        }

       
    }
}