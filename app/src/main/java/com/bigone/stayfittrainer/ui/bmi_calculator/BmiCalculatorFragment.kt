package com.bigOne.stayfittrainer.ui.bmi_calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigOne.stayfittrainer.ui.home.MainViewModel
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.UserData
import com.bigOne.stayfittrainer.databinding.FragmentBmiCalculatorBinding
import com.bigOne.stayfittrainer.utils.BMIUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BmiCalculatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BmiCalculatorFragment : Fragment() {
    lateinit var binding: FragmentBmiCalculatorBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bmi_calculator, container, false)
        return binding.root
    }
    val user = Firebase.auth.currentUser  // Replace with the actual user ID
  private var userData:UserData?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    private fun observe() {
        if (user != null) {
            mainViewModel.getUserData().observe(viewLifecycleOwner){userData->
                if(userData!=null){
                   val bmi = BMIUtils.calculateBMI(userData.weight.toDouble(), userData.height.toDouble())
                    this.userData=userData
                    showBMI(bmi)
                }
            }
        }
    }

    private fun showBMI(bmi :Double) {
            binding.calculateBmi.text = String.format("Your BMI: %.2f", bmi)
            changeBMIColor(bmi)
        }

    

    private fun changeBMIColor(bmi: Double) {
        binding.apply {
            if (bmi <= 18.5) {
                binding.underweightBmi.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
                binding.underweightBmiText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
                bmiResultDescrpition.text=" \uD83C\uDF1F Embrace your journey to better health! Despite being underweight, every effort towards nourishment and strength matters. Believe in your ability to thrive. Weight: ${userData?.weight}, Height: ${userData?.height}  \uD83D\uDCAA\uD83E\uDD66"
            } else if (bmi <= 24.9) {
                binding.normalWeight.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                binding.normalWeightText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
                bmiResultDescrpition.text="\uD83C\uDF1F Celebrate your balanced health! With a normal weight, continue nourishing your body and staying active. Keep up the great work! Height:${userData?.height} , Weight: ${userData?.weight}  \uD83D\uDCAA\uD83E\uDD57\n"


            } else if (bmi <= 29.9) {
                binding.overWeight.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                binding.overWeightText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                bmiResultDescrpition.text="\uD83C\uDF1F Acknowledge your BMI and prioritize health! Focus on balanced nutrition, exercise regularly, and seek guidance from healthcare professionals for support. You've got this!  Height:${userData?.height} , Weight: ${userData?.weight}\uD83D\uDCAA\uD83E\uDD66\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n"
            } else if (bmi <= 34.9) {
                binding.obesityWeight.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                binding.obeseText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                bmiResultDescrpition.text="\uD83C\uDF1F Acknowledge your BMI and prioritize health! Commit to balanced nutrition, regular exercise, and seek support from healthcare professionals. Every step forward counts! Height:${userData?.height} , Weight: ${userData?.weight}  \uD83D\uDCAA\uD83E\uDD66\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n"
            } else if (bmi <= 39.9) {
                binding.highlyObese.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor
                    )
                )
                binding.highlyObeseText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                bmiResultDescrpition.text="\uD83C\uDF1F Prioritize health by addressing over obesity. Embrace balanced nutrition, regular exercise, and consult healthcare professionals for personalized support and guidance. Your well-being matters! Height:${userData?.height} , Weight: ${userData?.weight}  \uD83D\uDCAA\uD83E\uDD66\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n"
            } else if (bmi >= 40) {
                binding.extremeObese.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor
                    )
                )
                binding.extremeObeseText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor))
                bmiResultDescrpition.text="\uD83C\uDF1F Embrace your journey towards wellness! With determination and support, every step you take towards better health is a victory. Believe in your strength and resilience. You're capable of remarkable transformations! Height:${userData?.height} , Weight: ${userData?.weight}  \uD83D\uDCAA\uD83E\uDD66\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n"
            }
        }
    }

    private fun init(){
        binding.nxtBtn.setOnClickListener {
            navigation()
        }

   }

    private fun navigation() {
        val action = BmiCalculatorFragmentDirections.actionBmiCalculatorToHomeFragment()
        findNavController().navigate(action);
    }
}