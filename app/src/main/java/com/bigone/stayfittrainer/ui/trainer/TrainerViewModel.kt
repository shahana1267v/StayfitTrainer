package com.bigOne.StayFitTrainer.ui.trainer


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bigOne.StayFitTrainer.datas.model.CourseData
import com.bigOne.StayFitTrainer.datas.model.WorkoutData
import com.bigOne.StayFitTrainer.datas.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth

class  TrainerViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())
    val user = FirebaseAuth.getInstance().currentUser
    private val _course = MutableLiveData<CourseData>()
    val course: LiveData<CourseData> = _course

    private val _workouts = MutableLiveData<List<WorkoutData>>()
    val workouts: LiveData<List<WorkoutData>> = _workouts

    private val _isSaveCourse = MutableLiveData<Boolean>(false)
    val isSaveCourse: LiveData<Boolean> = _isSaveCourse
   fun saveWorkOut(
       workout: String, time: String, difficulty: String, url: String, calorie: String
   ): LiveData<List<WorkoutData>> {
       val currentList = _workouts.value?.toMutableList() ?: mutableListOf()
        val workoutData =WorkoutData(workout,time,difficulty,url,calorie)
       currentList.add(workoutData)
       _workouts.value = currentList
       _isSaveCourse.value =false
       return workouts
    }

    fun saveCourse(courseName: String, totalTime: String,difficulty: String,descrp: String) {
        val courseData =CourseData(courseName,totalTime,difficulty,descrp,_workouts.value?: mutableListOf(),user!!.displayName.toString(),user.uid)
        Log.e("courseDate",courseData.toString())

        firebaseRepository.saveCourse(courseData)?.addOnSuccessListener {
            _isSaveCourse.value =true
        }?.addOnFailureListener {
            _isSaveCourse.value =false
        }

    }

    fun closeSave() {
        _isSaveCourse.value =false
        _workouts.value = mutableListOf()
    }

}

