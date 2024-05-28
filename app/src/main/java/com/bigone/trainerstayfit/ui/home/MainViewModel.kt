package com.bigOne.trainerstayfit.ui.home


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bigOne.trainerstayfit.datas.model.UserData
import com.bigOne.trainerstayfit.datas.repository.FirestoreRepository

class  MainViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())

    fun getUserData(): LiveData<UserData?> {
        val _savedUserData = MutableLiveData<UserData?> ()
        firebaseRepository.getUserData()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val item = document.data
                    Log.e("userData", item.toString())
                    if(item!=null) {
                        val userData = mapToUserData(item)
                        _savedUserData.value = userData
                    }else{
                        _savedUserData.value =null
                    }
                }
            }else{
                _savedUserData.value =null
            }
        }
        return _savedUserData
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
            isTrainer = data["trainer"] as Boolean

        )
    }





}

