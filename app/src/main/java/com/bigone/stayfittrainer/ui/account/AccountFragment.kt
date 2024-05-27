package com.bigOne.StayFitTrainer.ui.account

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.StayFitTrainer.R
import com.bigOne.StayFitTrainer.databinding.FragmentAccountBinding
import com.bigOne.StayFitTrainer.datas.model.UserData
import com.bigOne.StayFitTrainer.ui.home.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class AccountFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountBinding
    val user = Firebase.auth.currentUser
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()
        observe()

    }

    private fun observer() {
        mainViewModel.getUserData().observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isTrainer) {
                    binding.apply {
                        joinTrainer.visibility = View.GONE
                        trainerView.visibility = View.VISIBLE
                    }
                } else {
                    binding.apply {
                        joinTrainer.visibility = View.VISIBLE
                        trainerView.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observe() {
        if (user != null) {
            getUserData()
        }
        accountViewModel.isUpdateUser.observe(viewLifecycleOwner) {
            if (it) {
                getUserData()
            }
        }
    }


    private fun getUserData() {
        mainViewModel.getUserData().observe(viewLifecycleOwner) {
            if (it != null) {
                setData(it)
            }
        }
    }


    private fun setData(userData: UserData) {
        binding.heightNowText.text = "${userData!!.height} Cm"

    }

    private var nDialog: AddHeightFragment? = null

    private fun init() {
        binding.logoutButton.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToBmiFragment()
            findNavController().navigate(action)
        }
            binding.apply {
                joinTrainer.setOnClickListener {
                    val context: Context =
                        ContextThemeWrapper(requireContext(), R.style.DialogStyle)
                    MaterialAlertDialogBuilder(context)
                        .setMessage(resources.getString(R.string.join_as_a_trainer_text_confirm))
                        .setCancelable(false)
                        .setNeutralButton(resources.getString(R.string.general_no)) { _, _ ->
                        }
                        .setPositiveButton(resources.getString(R.string.general_yes)) { _, _ ->
                            accountViewModel.joinAsTrainer().observe(viewLifecycleOwner) {
                                Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_SHORT)
                                    .show()
                                binding.joinTrainer.visibility = View.GONE
                            }
                        }.show()
                }
                createCourse.setOnClickListener {
                    val action =
                        AccountFragmentDirections.actionAccountFragmentToCreateCourseFragment()
                    findNavController().navigate(action)
                }

                binding.AddHeight.setOnClickListener {
                    if (nDialog == null) nDialog = AddHeightFragment()
                    nDialog!!.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
                    nDialog?.show(requireActivity().supportFragmentManager, AddHeightFragment.TAG)
                }
            }
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






