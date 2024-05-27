package com.bigOne.StayFitTrainer.ui.Login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bigOne.StayFitTrainer.MainActivity
import com.bigOne.StayFitTrainer.R
import com.bigOne.StayFitTrainer.databinding.FragmentLoginSuccessBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginSuccessFragment:Fragment() {
    private lateinit var binding: FragmentLoginSuccessBinding
    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_success,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        getUserProfile()
    }

    private fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            binding.progressBar.visibility =View.GONE
            // Name, email address, and profile photo Url
            binding.userName.text = it.displayName
            binding.userEmail.text  = it.email
            if(it.photoUrl!=null) {
                Glide.with(this)
                    .load(it.photoUrl)
                    .placeholder(
                        AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_user_dp)!!)
                    .error(
                        AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_user_dp)!!)
                    .into(binding.userImage)

            }
            lifecycleScope.launch {
                delay(3000)
                navigate()
            }
           /* val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid*/
        }
    }

    private fun greetUser() {

    }

    private fun navigate() {
        startActivity(Intent(context,MainActivity::class.java))
        activity?.finish()
    }
}