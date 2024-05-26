package com.bigOne.stayfittrainer.utils

import android.content.Context
import android.util.Log
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.datas.model.UserData
import java.text.SimpleDateFormat
import java.util.Calendar

object BMIUtils {


    fun calculateBMI(weight: Double?, height: Double?): Double {
        return if (weight != null && height != null) {
            val heightInMeters = height / 100
            weight / (heightInMeters * heightInMeters)
        } else {
            0.0
        }


    }

    fun colorBMI( bmi: Double): Int {
        return when {
            bmi <= 18.5 -> R.color.colorInfo // Return color resource for underweight
            bmi <= 24.9 -> R.color.colorSuccessDark      // Return color resource for normal weight
            bmi <= 29.9 -> R.color.colorWarning  // Return color resource for overweight
            bmi <= 34.9 -> R.color.colorDanger  // Return color resource for obese class I
            bmi <= 39.9 -> R.color.colorDangerDark // Return color resource for obese class II
            bmi >= 40 -> R.color.colorDangerDarker
            else -> R.color.colorDangerDarker   // Return color resource for obese class III
        }
    }

    fun categoryBMI(context: Context, bmi: Double): String {
        return when {
            bmi <= 18.5 -> context.getString(R.string.underweight)// Return color resource for underweight
            bmi <= 24.9 ->  context.getString(R.string.normal)
            bmi <= 29.9 -> context.getString( R.string.overweight )
            bmi <= 34.9 -> context.getString( R.string.obese )
            bmi <= 39.9 -> context.getString( R.string.highly_obese )
            bmi >= 40 -> context.getString( R.string.extremely_obese )

            else -> context.getString( R.string.normal )
        }
    }

    fun calculateBMR(userData: UserData): Double {
        val age = calculateAge(userData.dob)
        Log.e("age",age.toString())
        val bmr = if (userData.sex.equals("Male", ignoreCase = true)) {
            // For males
            88.362 + (13.397 * userData.weight.toDouble()) + (4.799 * userData.height.toDouble()) - (5.677 * age!!)
        } else {
            // For females
            447.593 + (9.247 * userData.weight.toDouble()) + (3.098 * userData.height.toDouble()) - (4.330 * age!!)
        }
        return String.format("%.2f", bmr).toDouble()
    }

    fun calculateAge(dateString: String): Int {
        val format = SimpleDateFormat("EEE, dd-MM-yyyy")
        try {
            val date = format.parse(dateString)
            val dob = date.time
            val today = Calendar.getInstance().timeInMillis
            val ageYears = Math.floor((today - dob) / (365.25 * 24 * 60 * 60 * 1000.0)).toInt()
            return ageYears
        } catch (e: Exception) {
            return 0
        }
    }

}