package com.bigOne.trainerstayfit.datas.model




data class RecipeDetails(
    val recipe: RecipeData
)

data class RecipeData(
    val cooking_time_min: String,
    val directions: Directions,
    val grams_per_portion: String,
    val ingredients: Ingredients,
    val number_of_servings: String,
    val preparation_time_min: String,
    val rating: String,
    val recipe_categories: RecipeCategories,
    val recipe_description: String,
    val recipe_id: String,
    val recipe_images: RecipeImages,
    val recipe_name: String,
    val recipe_types: RecipeTypesDetail,
    val recipe_url: String,
    val serving_sizes: ServingSizes
)

data class Directions(
    val direction: List<Direction>
)

data class Ingredients(
    val ingredient: List<Ingredient>
)

data class RecipeCategories(
    val recipe_category: List<RecipeCategory>
)

data class RecipeImages(
    val recipe_image: List<String>
)

data class RecipeTypesDetail(
    val recipe_type: List<String>
)

data class ServingSizes(
    val serving: ServingDetail
)

data class Direction(
    val direction_description: String,
    val direction_number: String
)

data class Ingredient(
    val food_id: String,
    val food_name: String,
    val ingredient_description: String,
    val ingredient_url: String,
    val measurement_description: String,
    val number_of_units: String,
    val serving_id: String
)

data class RecipeCategory(
    val recipe_category_name: String,
    val recipe_category_url: String
)

data class ServingDetail(
    val calcium: String,
    val calories: String,
    val carbohydrate: String,
    val cholesterol: String,
    val fat: String,
    val fiber: String,
    val iron: String,
    val monounsaturated_fat: String,
    val polyunsaturated_fat: String,
    val potassium: String,
    val protein: String,
    val saturated_fat: String,
    val serving_size: String,
    val sodium: String,
    val sugar: String,
    val trans_fat: String,
    val vitamin_a: String,
    val vitamin_c: String
)