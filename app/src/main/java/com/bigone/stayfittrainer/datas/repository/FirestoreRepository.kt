package com.bigOne.stayfitadmin.datas.repository

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot





class FirestoreRepository(c :Application) {

    private fun activityViewModels() {

    }

    var db = FirebaseFirestore.getInstance()


    companion object {
        const val USER_FIREBASE_DATA_KEY = "users"
    }


    fun getUsersData(): Task<QuerySnapshot> {
        val collectionReference = db.collection(USER_FIREBASE_DATA_KEY).get()
        return collectionReference
    }

    fun approvedTrainer(id: String): Task<Unit>
    {

            val userDocumentRef =  db.collection(USER_FIREBASE_DATA_KEY).document(id)
            if (userDocumentRef != null) {
                return userDocumentRef.get().continueWith { task ->
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        try {
                            var data = documentSnapshot.data
                            val userData = data?.let { mapToUserData(it) }
                            if (userData != null) {
                                userData.approved= true
                                userDocumentRef.set(userData)
                            } else {
                                throw IllegalStateException("Failed to parse user data")
                            }
                        }catch (e: Exception){
                            Log.e("Exp",e.toString())
                        }
                    }
                }
            }else{
                throw IllegalStateException("User document not found or doesn't exist")
            }
        }

    private fun mapToUserData(data: Map<String, Any>): UserData {
        return UserData(
            sex = data["sex"] as String,
            dob = data["dob"] as String,
            weight = data["weight"] as String,
            height = data["height"] as String,
            name = data["name"] as String,
            email = data["email"] as String,
            id = data["id"] as String,
            img = data["img"] as String,
            isTrainer = data["trainer"] as Boolean,
            approved = data["approved"] as Boolean
        )
    }


}







