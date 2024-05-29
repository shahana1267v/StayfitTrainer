package com.bigOne.trainerstayfit.datas.repository

import android.app.Application
import android.util.Log
import com.bigOne.trainerstayfit.datas.model.CourseData
import com.bigOne.trainerstayfit.datas.model.SavedFood
import com.bigOne.trainerstayfit.datas.model.UserData
import com.bigOne.trainerstayfit.utils.DateUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository(c :Application) {
    val TAG = "FIREBASE_REPOSITORY"
    var db = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    companion object {
        const val USER_FIREBASE_DATA_KEY = "users"
        const val FOOD_LOG_KEY = "food_logs"
        const val FIREBASE_COURSE = "course"
    }

    fun saveUserData(userData: UserData): Task<Void>? {
        user?.let {
            var documentReference =
                db.collection(USER_FIREBASE_DATA_KEY).document(it.uid).set(userData)
            return documentReference
        }
        return null
    }

    // get saved addresses from firebase
    fun getUserData(): Task<DocumentSnapshot>? {
        user?.let {
            val collectionReference =
                db.collection(USER_FIREBASE_DATA_KEY).document(user!!.uid).get()
            return collectionReference
        }
        return null
    }


    fun joinAsTrainer(): Task<Unit> {
        val userDocumentRef = user?.let { db.collection(USER_FIREBASE_DATA_KEY).document(it.uid) }
        if (userDocumentRef != null) {
            return userDocumentRef.get().continueWith { task ->
                val documentSnapshot = task.result
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    try {
                        var data = documentSnapshot.data
                        val userData = data?.let { mapToUserData(it) }
                        if (userData != null) {
                            userData.isTrainer = true
                            userDocumentRef.set(userData)
                        } else {
                            throw IllegalStateException("Failed to parse user data")
                        }
                    } catch (e: Exception) {
                        Log.e("Exp", e.toString())
                    }
                }
            }
        } else {
            throw IllegalStateException("User document not found or doesn't exist")
        }
    }



    fun saveCourse(courseData: CourseData): Task<Void>? {
        user?.let {
            val documentReference = db.collection(FIREBASE_COURSE).document()
            return documentReference.set(courseData)
        }
        return null
    }

    fun getCourses(): Task<QuerySnapshot> {
        return db.collection(FIREBASE_COURSE).get()

    }


}


    private fun mapToUserData(data: Map<String, Any>): UserData {
        return UserData(

            name = data["name"] as String,
            email = data["email"] as String,
            id = data["id"] as String,
            img = data["img"] as String,
            isTrainer = data["trainer"] as Boolean,
            approved = data["approved"] as Boolean,
            qualification = data["qualification"] as String?,
            experience =  data["experience"] as String?
        )
    }


