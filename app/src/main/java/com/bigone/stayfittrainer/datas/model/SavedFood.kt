  package com.bigOne.StayFitTrainer.datas.model

data class SavedFood(
    val food_id:String,
    val mealType: Int,
    val name: String,
    val selectedQuantity: String,
    val selectedUnit: String,
    val calorie: Double
)