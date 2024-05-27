package com.bigOne.trainerstayfit.datas.model




data class RecipeResponse(
    val recipes: Recipes
)

data class Recipes(
    val max_results: String,
    val page_number: String,
    val recipe: List<Recipe>,
    val total_results: String
)

data class Recipe(
    val recipe_description: String,
    val recipe_id: String,
    val recipe_image: String,
    val recipe_ingredients: RecipeIngredients,
    val recipe_name: String,
    val recipe_nutrition: RecipeNutrition,
    val recipe_types: RecipeTypes
)

data class RecipeIngredients(
    val ingredient: List<String>
)

data class RecipeNutrition(
    val calories: String,
    val carbohydrate: String,
    val fat: String,
    val protein: String
)

data class RecipeTypes(
    val recipe_type: List<String>
)