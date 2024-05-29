package com.bigone.trainerstayfit.ui.Register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.bigOne.trainerstayfit.R
import com.bigOne.trainerstayfit.databinding.FragmentMyCourseBinding
import com.bigOne.trainerstayfit.databinding.FragmentRegisterBinding
import com.bigOne.trainerstayfit.datas.model.UserData
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    val user = Firebase.auth.currentUser
    var userData: UserData? = null


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


    }
private fun init()
{
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
}

}

