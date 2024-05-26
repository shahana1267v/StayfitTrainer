package com.bigOne.stayfittrainer.ui.Recipe
import android.app.Application
import androidx.lifecycle.*
import com.bigOne.stayfittrainer.datas.model.Recipe
import com.bigOne.stayfittrainer.datas.model.RecipeData
import com.bigOne.stayfittrainer.datas.repository.FirestoreRepository
import com.bigOne.stayfittrainer.datas.repository.RecipelogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class  RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepo = RecipelogRepository(getApplication())

    private val firestoreRepository = FirestoreRepository(getApplication())
    private val _recipeData = MutableLiveData<List<Recipe?>?>()
    val recipeData: MutableLiveData<List<Recipe?>?> = _recipeData

    private val _recipeDataDetails = MutableLiveData<RecipeData?>()
    val recipeDataDetails: MutableLiveData<RecipeData?> = _recipeDataDetails

    private val _dismissDialog = MutableLiveData<Boolean>(false)
    val dismissDialog: LiveData<Boolean> = _dismissDialog


    fun searchRecipe(query: String?): MutableLiveData<List<Recipe?>?> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = recipeRepo.searchRecipe(query)
            _recipeData.postValue(response)
        }
        return recipeData
    }

    fun closeDialog() {
        _dismissDialog.value=false
    }

    fun getRecipeDetails(id: String): MutableLiveData<RecipeData?> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = recipeRepo.getRecipeDetails(id)
            _recipeDataDetails.postValue(response)

        }
        return recipeDataDetails
    }




}




