package com.bigone.trainerstayfit.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.trainerstayfit.R
import com.bigOne.trainerstayfit.databinding.FragmentHomeBinding
import com.bigOne.trainerstayfit.ui.home.MainViewModel
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()

    }
    private fun init()
    {
           binding.createcourse.setOnClickListener{
               val action = HomeFragmentDirections.actionHomeFragmentToCreateCourseFragment()
               findNavController().navigate(action)
           }
        binding.accName.text = user?.displayName.toString()

        if (user?.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_user_dp
                    )!!
                )
                .error(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_user_dp
                    )!!
                )
                .into(binding.accImage)

        }


    }
    private fun observer() {
        mainViewModel.getUserData().observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isTrainer) {
                    binding.apply {
                        createcourse.visibility=View.VISIBLE
                        joinView.visibility =View.GONE
                    }
                } else {
                    binding.apply {
                        createcourse.visibility=View.GONE
                        joinView.visibility =View.VISIBLE
                    }
                }
            }
        }
    }


}
