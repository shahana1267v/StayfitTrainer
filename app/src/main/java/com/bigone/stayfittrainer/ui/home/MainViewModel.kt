package com.bigOne.StayFitTrainer.ui.home


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bigOne.StayFitTrainer.datas.model.UserData
import com.bigOne.StayFitTrainer.datas.repository.FirestoreRepository
import com.bigOne.StayFitTrainer.datas.repository.MeallogRepository

class  MainViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirestoreRepository = FirestoreRepository(getApplication())







}

