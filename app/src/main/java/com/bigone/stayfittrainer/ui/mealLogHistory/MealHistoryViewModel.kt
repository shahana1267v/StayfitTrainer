package com.bigOne.stayfittrainer.ui.mealLogHistory
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bigOne.stayfittrainer.datas.model.SavedFood
import com.bigOne.stayfittrainer.datas.repository.FirestoreRepository
import com.bigOne.stayfittrainer.datas.repository.MeallogRepository
import com.bigOne.stayfittrainer.utils.DateUtils
import java.util.Date


class  MealHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepo = MeallogRepository(getApplication())

    private val firestoreRepository = FirestoreRepository(getApplication())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    private val _dismissDialog = MutableLiveData<Boolean>(false)
    val dismissDialog: LiveData<Boolean> = _dismissDialog


    private val _savedFoods = MutableLiveData<List<SavedFood>>()
    val savedFoods: LiveData<List<SavedFood>> = _savedFoods
    fun getSelectedDateFoodList(date: Date): LiveData<List<SavedFood>> {
        val formatedDate = DateUtils.formatDate(date)
        _savedFoods.value = mutableListOf()
        formatedDate?.let {
            firestoreRepository.getFoodDataForDate(it)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val savedFoods = mutableListOf<SavedFood>()
                    val documents = task.result?.documents
                    if (documents != null) {
                        for (document in documents) {
                            val foodData = document.data
                            val foodsArray = foodData?.get("foods") as? List<Map<String, Any>>?
                            if (foodsArray != null) {
                                for (foodMap in foodsArray) {
                                    Log.e("foodDataABC", foodsArray.toString())
                                    val foodId = foodMap["food_id"].toString()
                                    val mealType = (foodMap["mealType"].toString()).toInt()
                                    val name = foodMap["name"].toString()
                                    val selectedQuantity =
                                        foodMap.get("selectedQuantity").toString()
                                    val selectedUnit = foodMap.get("selectedUnit").toString()
                                    val calorie = (foodMap.get("calorie") as Double)

                                    val savedFood = SavedFood(
                                        foodId,
                                        mealType,
                                        name,
                                        selectedQuantity,
                                        selectedUnit,
                                        calorie
                                    )
                                    savedFoods.add(savedFood)
                                }
                            }
                        }
                        _savedFoods.value = savedFoods

                    } else {
                        _savedFoods.value = mutableListOf()
                        Log.e("Error", "Failed to retrieve food data for date")
                    }
                }
            }

        }
        return savedFoods
    }
}

