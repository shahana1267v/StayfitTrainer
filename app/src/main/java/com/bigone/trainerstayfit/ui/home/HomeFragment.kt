package com.bigone.trainerstayfit.ui.home

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.trainerstayfit.R
import com.bigOne.trainerstayfit.databinding.FragmentHomeBinding
import com.bigOne.trainerstayfit.datas.model.UserData
import com.bigOne.trainerstayfit.ui.home.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
   private val user = Firebase.auth.currentUser
   private var userData: UserData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        init()


    }
    private fun init() {
        binding.joinBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRegisterFragment()
            findNavController().navigate(action)
        }




        binding.createcourse.setOnClickListener {
            if (userData?.approved == true) {
                val action = HomeFragmentDirections.actionHomeFragmentToCreateCourseFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Account not verified!, Please wait untill admin verify Account", Toast.LENGTH_LONG).show()
            }
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
                userData =it
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
