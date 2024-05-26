package com.bigOne.stayfittrainer.ui.bmi_calculator

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.stayfittrainer.ui.home.MainViewModel
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.UserData
import com.bigOne.stayfittrainer.databinding.BmiFragmentBinding
import com.bigOne.stayfittrainer.utils.CommonUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class BmiFragment : Fragment() {

    lateinit var binding: BmiFragmentBinding
    var selectedItem = " "
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.bmi_fragment, container, false)
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genderOptions)
        binding.genderList.setAdapter(adapter)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    private fun observe() {
        mainViewModel.getUserData().observe(viewLifecycleOwner){
            Log.e("getUserData",it.toString())
            if(it!=null)
              navigation()
        }
        mainViewModel.isSavedUser.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
            navigation()
        }

    }

    private fun init() {
        binding.dobField.editText?.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDatePicker() }
            setOnClickListener { showDatePicker() }

            }
            binding.genderList.setOnItemClickListener { parent, view, position, id ->
                selectedItem = parent.getItemAtPosition(position).toString()
            }
            binding.saveBtn.setOnClickListener {
                binding.apply {
                    if (dobEditText.text?.isEmpty() == true) {
                        dobField.error = " Required Field"
                    } else if (weightEditText.text?.isEmpty() == true) {
                        weightField.error = "Required"

                    } else if (heightEditText.text?.isEmpty() == true) {
                        heightField.error = "Required Field"
                    } else {
                        try {
                            var user = FirebaseAuth.getInstance().currentUser
                            val userData = UserData(
                                sex = selectedItem,
                                dobEditText.text.toString(),
                                weightEditText.text.toString(),
                                heightEditText.text.toString(),
                                name = user?.displayName.toString(),
                                email = user?.email.toString(),
                                img = user?.photoUrl.toString(),
                                id = user?.uid.toString(),
                                isTrainer = false,
                            )
                            mainViewModel.saveUserData(userData)
                        } catch (e: Exception) {
                            Log.e("DocumentSnapshot", e.toString())
                        }

                    }

                }
            }
        }
    private fun navigation() {
        val action = BmiFragmentDirections.actionBmiFragmentToBmiCalculator()
        findNavController().navigate(action);
    }

    private fun showDatePicker() {
        val date: LocalDate = LocalDate.now()
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText(R.string.date_of_birth_select_text)
        builder.setTheme(R.style.ThemeMaterialCalendar)
        val dateLong = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        builder.setSelection(dateLong)
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setOpenAt(dateLong)
        builder.setCalendarConstraints(constraintsBuilder.build())
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            val newDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
            binding.dobEditText.setText(CommonUtils.getReadableDate(newDate))
        }
        picker.show(childFragmentManager,"CHECKUP_DATE")
    }


}