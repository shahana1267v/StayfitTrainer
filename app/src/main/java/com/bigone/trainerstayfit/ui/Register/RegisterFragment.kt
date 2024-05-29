package com.bigone.trainerstayfit.ui.Register

import android.os.Bundle
import android.util.Log
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
import com.bigOne.trainerstayfit.databinding.FragmentMyCourseBinding
import com.bigOne.trainerstayfit.databinding.FragmentRegisterBinding
import com.bigOne.trainerstayfit.datas.model.UserData
import com.bigOne.trainerstayfit.ui.home.MainViewModel
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    val user = Firebase.auth.currentUser
    var userData: UserData? = null
    private val mainViewModel: MainViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()

    }

    private fun observer() {
      mainViewModel.isSavedUser.observe(viewLifecycleOwner){
          if(it){
              Toast.makeText(requireContext(), "Successfully joined", Toast.LENGTH_SHORT).show()
              findNavController().popBackStack()
          }
      }
    }

    private fun init() {
    binding.accName.text = user?.displayName.toString()
    binding.accEmail.text = user?.email.toString()
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

    binding.savebtn.setOnClickListener {
        binding.apply {
            if (Qualification.editText?.text?.isEmpty() == true) {
                Qualification.error = " Required Field"
            } else if (Experience.editText?.text?.isEmpty() == true) {
                Experience.error = "Required"

            }  else {
                try {
                    var user = FirebaseAuth.getInstance().currentUser
                    val userData = UserData(
                        qualification = Qualification.editText?.text?.toString(),
                         experience = Experience.editText?.text?.toString(),
                        name = user?.displayName.toString(),
                        email = user?.email.toString(),
                        img = user?.photoUrl.toString(),
                        id = user?.uid.toString(),
                        isTrainer = true,
                    )
                    mainViewModel.saveUserData(userData)
                } catch (e: Exception) {
                    Log.e("DocumentSnapshot", e.toString())
                }

            }

        }
    }
}








}



