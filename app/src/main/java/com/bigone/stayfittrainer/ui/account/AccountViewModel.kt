package com.bigOne.StayFitTrainer.ui.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bigOne.StayFitTrainer.datas.model.UserData
import com.bigOne.StayFitTrainer.datas.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isUpdateTrainer = MutableLiveData<Boolean>(false)
    val isUpdateTrainer: LiveData<Boolean> = _isUpdateTrainer

    private val _isSavedUser = MutableLiveData<Boolean>()
    val isSavedUser: LiveData<Boolean> = _isSavedUser



    private val _isUpdateUser = MutableLiveData<Boolean>(false)
    val isUpdateUser: LiveData<Boolean> = _isUpdateUser

    fun refreshProfile() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }


    fun joinAsTrainer(): LiveData<Boolean> {
        firebaseRepository.joinAsTrainer().addOnSuccessListener {
            _isUpdateTrainer.value = true
        }.addOnFailureListener {
            _isUpdateTrainer.value = false
        }
        return isUpdateTrainer
    }
    fun saveUserData(userData: UserData){

        firebaseRepository.saveUserData(userData)?.addOnSuccessListener {
            _isSavedUser.value =true
        }?.addOnFailureListener {
            _isSavedUser.value =false
        }
    }


    fun getUserData(): LiveData<UserData?> {
        val _savedUserData = MutableLiveData<UserData?>()
        firebaseRepository.getUserData()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val item = document.data
                    Log.e("userData", item.toString())
                    if (item != null) {
                        val userData = mapToUserData(item)
                        _savedUserData.value = userData
                    } else {
                        _savedUserData.value = null
                    }
                }
            } else {
                _savedUserData.value = null
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

    fun UpdateHeight(height: String) {
        firebaseRepository.updatedData(height).addOnSuccessListener {
            _isUpdateUser.value = true
        }.addOnFailureListener {
            _isUpdateUser.value = false
        }


    }


    fun clearUpdateUser() {
        _isUpdateUser.value = false
    }
}


