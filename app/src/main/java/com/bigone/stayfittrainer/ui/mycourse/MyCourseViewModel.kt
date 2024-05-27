package com.bigOne.StayFitTrainer.ui.mycourse


import android.app.Application
import androidx.lifecycle.*
import com.bigOne.StayFitTrainer.datas.model.CourseData
import com.bigOne.StayFitTrainer.datas.model.WorkoutData
import com.bigOne.StayFitTrainer.datas.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class  MyCourseViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())
    val user = FirebaseAuth.getInstance().currentUser
    private val _course = MutableLiveData<List<CourseData>>()
    val course: LiveData<List<CourseData>> = _course

    private val _workouts = MutableLiveData<List<WorkoutData>>()
    val workouts: LiveData<List<WorkoutData>> = _workouts
    private val _courses = MutableLiveData<List<CourseData>>()
    val courses: LiveData<List<CourseData>> = _courses

    private val _workout = MutableLiveData<List<WorkoutData>>()
    val workout: LiveData<List<WorkoutData>> = _workout


     fun getMyCourses(): LiveData<List<CourseData>> {
        val courses = mutableListOf<CourseData>()
        viewModelScope.launch(Dispatchers.IO) {
            val querySnapshot = firebaseRepository.getCourses().await()
            for (document in querySnapshot.documents) {
                val data = document.data
                if (data != null) {
                    val courseData = mapToCourseData(data)
                    courseData?.let { courses.add(it) }
                }
            }
            _course.postValue(courses)
        }
        return course
    }
    fun getMyCourse(): LiveData<List<CourseData>> {
        val course = mutableListOf<CourseData>()
        viewModelScope.launch(Dispatchers.IO) {
            val querySnapshot = firebaseRepository.getCourses().await()
            for (document in querySnapshot.documents) {
                val data = document.data
                if (data != null) {
                    val courseData = mapToCourseData(data)
                    courseData?.let { course.add(it) }
                }
            }
            _course.postValue(course)
        }
        return courses
    }

    private fun mapToCourseData(data: Map<String, Any>): CourseData? {
        val couresName = data["couresName"] as? String ?: return null
        val totalTime = data["totalTime"] as? String ?: return null
        val difficulty = data["difficulty"] as? String ?: ""
        val description = data["description"] as? String ?: ""
        val workouts = (data["workouts"] as? List<*>)?.mapNotNull { mapToWorkoutData(it as? Map<String, Any>) } ?: emptyList()
        val trainerName = data["trainerName"] as? String
        val trainerId = data["trainerId"] as? String
        return CourseData(couresName, totalTime, difficulty, description, workouts, trainerName, trainerId)
    }

    private fun mapToWorkoutData(data: Map<String, Any>?): WorkoutData? {
        if (data == null) return null
        val name = data["name"] as? String ?: return null
        val time = data["time"] as? String ?: return null
        val difficulty = data["difficulty"] as? String ?: ""
        val url = data["url"] as? String ?: ""
        val calorie = data["calorie"] as? String ?: ""
        return WorkoutData(name, time, difficulty, url, calorie)
    }


}

