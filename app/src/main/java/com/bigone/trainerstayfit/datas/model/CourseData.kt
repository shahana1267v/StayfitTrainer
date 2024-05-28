package com.bigOne.trainerstayfit.datas.model

import com.bigOne.trainerstayfit.datas.model.WorkoutData

data class CourseData(

    val couresName: String,
    val totalTime: String,
    var difficulty: String,
    val description: String,
    val workouts:List<WorkoutData>,
    val trainerName: String?,
    val trainerId :String?,
)