package com.bigOne.StayFitTrainer.datas.model

data class CourseData(

    val couresName: String,
    val totalTime: String,
    var difficulty: String,
    val description: String,
    val workouts:List<WorkoutData>,
    val trainerName: String?,
    val trainerId :String?,
)