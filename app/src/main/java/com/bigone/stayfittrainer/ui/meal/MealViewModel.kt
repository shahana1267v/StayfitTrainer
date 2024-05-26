package com.bigOne.stayfittrainer.ui.meal
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bigOne.stayfittrainer.datas.model.FoodDetails
import com.bigOne.stayfittrainer.datas.model.FoodsResponse
import com.bigOne.stayfittrainer.datas.model.SavedFood
import com.bigOne.stayfittrainer.datas.model.Serving
import com.bigOne.stayfittrainer.datas.repository.FirestoreRepository
import com.bigOne.stayfittrainer.datas.repository.MeallogRepository
import com.bigOne.stayfittrainer.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class  MealViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepo = MeallogRepository(getApplication())

    private val firestoreRepository = FirestoreRepository(getApplication())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading
    private val _mealData = MutableLiveData<FoodsResponse?>()
    val mealData: MutableLiveData<FoodsResponse?> = _mealData

    private val _todayCalorie= MutableLiveData<String?>()
    val todayCalorie: LiveData<String?> = _todayCalorie

    private val _dismissDialog = MutableLiveData<Boolean>(false)
    val dismissDialog: LiveData<Boolean> = _dismissDialog

    private val _getALreadyLoggedFood = MutableLiveData<List<SavedFood>>()
    val getALreadyLoggedFood: LiveData<List<SavedFood>> = _getALreadyLoggedFood

    private val _mealDataDeatils = MutableLiveData<FoodDetails?>()
    val mealDataDeatils: MutableLiveData<FoodDetails?> = _mealDataDeatils

    fun searchMeal(query: String): MutableLiveData<FoodsResponse?> {
        Log.e("query Search",query)
        viewModelScope.launch(Dispatchers.IO) {
            val response = mealRepo.searchMeal(query)
            _mealData.postValue(response)
           Log.e("queryResponse",response?.foods.toString())
            }
        return mealData
        }

    fun getMealDetails(id: String): MutableLiveData<FoodDetails?> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mealRepo.getMealDetails(id)
            _mealDataDeatils.postValue(response)

        }
       return mealDataDeatils
    }

    private val _savedFoods = MutableLiveData<List<SavedFood>>()
    val savedFoods: LiveData<List<SavedFood>> = _savedFoods
    fun saveFood(quantity: String, servingData: Serving, servingSize: String, mealType: Int, foodDetails: FoodDetails) {
        val currentList = _savedFoods.value?.toMutableList() ?: mutableListOf()
         val savedFood = SavedFood(food_id = foodDetails.food_id, name = foodDetails.food_name, mealType = mealType, selectedQuantity = quantity, selectedUnit = servingSize, calorie = servingData.calories.toDouble() )
        currentList.add(savedFood)
        _savedFoods.value = currentList
        Log.e("currentFood",currentList.toString())
        _dismissDialog.value =true
    }

    fun getalreadyMealDetails(mealType: Int) {
        val currentList = _savedFoods.value?.toMutableList() ?: mutableListOf()
        firestoreRepository.getAlreadySavedFood(mealType).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val item = document.data
                    if (item != null) {
                        val foodData = mapToFoodData(item)
                        if (foodData.isNotEmpty()) {
                            if (_getALreadyLoggedFood.value!=foodData && mealType == foodData[0].mealType){
                                currentList.addAll(foodData)
                                _getALreadyLoggedFood.value =foodData
                            }else{
                                return@addOnCompleteListener
                            }
                        }
                    } else {
                        Log.e("LogFood", "NO FOOD")
                    }
                }
            }
        }
        _savedFoods.value = currentList
    }

   fun  getCurrentDateFoodList(): LiveData<String?> {
       val currentDate = DateUtils.getCurrentDate()
       firestoreRepository.getFoodDataForDate(currentDate)?.addOnCompleteListener { task ->
           if (task.isSuccessful) {
               val documents = task.result?.documents
               if (documents != null) {
                   var totalCalories = 0.0
                   for (document in documents) {
                       val foods = document["foods"] as? List<Map<String, Any>>?
                       foods?.forEach { food ->
                           val calorie = food["calorie"] as? Double
                           val name =food["name"] as String

                           val selectedQuantity = food["selectedQuantity"] as? String
                           Log.e("name","${name} :${selectedQuantity!!.toDouble() * calorie!!}" )
                           if (calorie != null && selectedQuantity != null) {
                               totalCalories += selectedQuantity.toDouble() * calorie
                           }
                       }
                   }
                   Log.e("Total Calories", totalCalories.toString())
                   _todayCalorie.value =totalCalories.toString()
               }
           } else {
               Log.e("Error", "Failed to retrieve food data for date")
           }
       }
       return todayCalorie
   }

    private fun mapToFoodData(data: Map<String, Any>?): MutableList<SavedFood> {
        val mealData = mutableListOf<SavedFood>()

        // Check if the data map is not null and contains the key "foods"
        if (data != null && data.containsKey("foods")) {
            val foodsList = data["foods"] as? List<Map<String, Any>> // Get the list of food items

            // Iterate over each food item in the list and map it to a SavedFood object
            foodsList?.forEach { foodItem ->
                try {
                    val food = SavedFood(
                        food_id = foodItem["food_id"] as String,
                        mealType = (foodItem["mealType"] as Long).toInt(),
                        name = foodItem["name"] as String,
                        selectedQuantity = foodItem["selectedQuantity"].toString(), // Ensure selectedQuantity is a String
                        selectedUnit = foodItem["selectedUnit"] as String,
                        calorie = foodItem["calorie"] as Double
                    )
                    mealData.add(food)
                } catch (e: Exception) {
                    Log.e("Error", "Error parsing food item: $e")
                }
            }
        } else {
            Log.e("Error", "Invalid data format: $data")
        }

        return mealData
    }

    fun closeDialog() {
        _dismissDialog.value=false
    }

    private val _isfoodLogged = MutableLiveData<Boolean>()
    val isFoodLogged: LiveData<Boolean> = _isfoodLogged

    fun saveFoodDb(): LiveData<Boolean> {
        if (!_savedFoods.value.isNullOrEmpty()) {
            firestoreRepository.logFood(_savedFoods.value!!).addOnSuccessListener {
                _isfoodLogged.value = true
            }.addOnFailureListener {
                _isfoodLogged.value = false
            }
        }
        return isFoodLogged
    }

    fun clearSavedFood() {
        _savedFoods.value = mutableListOf()
        _getALreadyLoggedFood.value = mutableListOf()
    }

    fun setfood(it1: List<SavedFood>) {
         clearSavedFood()
        _savedFoods.value=it1
    }

    fun deletefood(item: SavedFood) {
        val updatedList = _savedFoods.value?.toMutableList() ?: mutableListOf()
        updatedList.remove(item)
       _savedFoods.value = updatedList
    }

}




