package com.bigOne.trainerstayfit.ui.home


import android.app.Application
import androidx.lifecycle.*
import com.bigOne.trainerstayfit.datas.repository.FirestoreRepository

class  MainViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())







}

