package com.bigOne.StayFitTrainer.datas.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutData(
    val name: String,
    val time: String,
    var difficulty: String,
    val url: String,
    val calorie:String
): Parcelable