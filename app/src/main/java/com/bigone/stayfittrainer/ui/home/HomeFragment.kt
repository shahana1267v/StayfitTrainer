package com.bigOne.stayfittrainer.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.UserData
import com.bigOne.stayfittrainer.databinding.FragmentHomeBinding
import com.bigOne.stayfittrainer.ui.bmi_calculator.AddWeightFragment
import com.bigOne.stayfittrainer.ui.meal.MealTypeFragment
import com.bigOne.stayfittrainer.ui.meal.MealViewModel
import com.bigOne.stayfittrainer.utils.BMIUtils
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val mealViewModel: MealViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    val user = Firebase.auth.currentUser
    var userData: UserData? =null
    var totalCaloriesTaken = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        init()
    }
    private fun observe() {
        if (user != null) {
           getUserData()
        }
        mainViewModel.isUpdateUser.observe(viewLifecycleOwner) {
            if(it) {
                getUserData()
            }
        }
        mealViewModel.getCurrentDateFoodList().observe(viewLifecycleOwner){
            if(!it.isNullOrBlank()){
                totalCaloriesTaken = it.toDouble()
                getUserData()
            }
        }
    }

    private fun getUserData() {
        mainViewModel.getUserData().observe(viewLifecycleOwner){
            if(it!=null){
                setData(it)
            }else{
                val action = HomeFragmentDirections.actionHomeFragmentToBmiFragment()
                findNavController().navigate(action);
            }
        }
        mainViewModel.getAuthToken()
    }

    private fun setData(userData: UserData) {
        binding.weightNowText.text ="${userData!!.weight} Kg"
        val bmi = BMIUtils.calculateBMI(userData.weight.toDouble(), userData.height.toDouble())
        binding.bmiText.text =String.format("%.2f", bmi)
        changeColor(bmi)

        val bmr =BMIUtils.calculateBMR(userData)
        Log.e("BMR",bmr.toString())
        if(totalCaloriesTaken<=0.0){
            binding.calEtanTitle.text = getString(R.string.cal_eaten_title)
            binding.totalCalorie.visibility =View.GONE
        }else{
            binding.calEtanTitle.text = getString(R.string.calorie_text)
            binding.totalCalorie.visibility =View.VISIBLE
            binding.totalCalorie.text="${totalCaloriesTaken.toInt()} of "
        }

        binding.calorie.text = "${bmr} Calorie"
    }

    private fun changeColor(bmi: Double) {
         val color = BMIUtils.colorBMI(bmi)
         val bmiCategory =BMIUtils.categoryBMI(requireContext(),bmi)
        binding.bmiTextStage.text =bmiCategory
        binding.bmiTextStage.setTextColor( ContextCompat.getColor(requireContext(), color))
    }
    private var mDialog: MealTypeFragment? = null
    private var nDialog: AddWeightFragment? = null
    private  fun  init(){
        binding.userName.text =user?.displayName.toString()
        if(user?.photoUrl!=null) {
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(
                    AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_user_dp)!!)
                .error(
                    AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_user_dp)!!)
                .into(binding.poster)

        }

        binding.seeMore.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBmiCalculator()
            findNavController().navigate(action)
        }

        binding.logMealBtn.setOnClickListener {
            if (mDialog == null) mDialog = MealTypeFragment()
            mDialog!!.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
            mDialog?.show(requireActivity().supportFragmentManager, MealTypeFragment.TAG)

        }

        binding.AddWeight.setOnClickListener {
           if (nDialog == null) nDialog = AddWeightFragment()
            nDialog!!.setStyle(DialogFragment.STYLE_NO_TITLE,R.style.DialogStyle)
            nDialog?.show(requireActivity().supportFragmentManager,AddWeightFragment.TAG)
        }
        binding.logCard.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMealLogHistoryFragment()
            findNavController().navigate(action);
        }
    }

}