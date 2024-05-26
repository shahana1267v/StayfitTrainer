package com.bigOne.stayfittrainer.datas.model

import com.google.gson.annotations.SerializedName

data class FoodDetailsData(
    @SerializedName("food")
    val foodDetails: FoodDetails
)

data class FoodDetails(
    val food_attributes: FoodAttributes,
    val food_id: String,
    val food_images: FoodImages,
    val food_name: String,
    val food_sub_categories: FoodSubCategories,
    val food_type: String,
    val food_url: String,
    val servings: Servings
)

data class FoodAttributes(
    val allergens: Allergens,
    val preferences: Preferences
)

data class FoodImages(
    val food_image: List<FoodImage>
)

data class FoodSubCategories(
    val food_sub_category: List<String>
)

data class Servings(
    val serving: List<Serving>
)

data class Allergens(
    val allergen: List<Allergen>
)

data class Preferences(
    val preference: List<Preference>
)

data class Allergen(
    val id: String,
    val name: String,
    val value: String
)

data class Preference(
    val id: String,
    val name: String,
    val value: String
)

data class FoodImage(
    val image_type: String,
    val image_url: String
)

data class Serving(
    val calcium: String,
    val calories: String,
    val carbohydrate: String,
    val cholesterol: String,
    val fat: String,
    val fiber: String,
    val iron: String,
    val measurement_description: String,
    val metric_serving_amount: String,
    val metric_serving_unit: String,
    val monounsaturated_fat: String,
    val number_of_units: String,
    val polyunsaturated_fat: String,
    val potassium: String,
    val protein: String,
    val saturated_fat: String,
    val serving_description: String,
    val serving_id: String,
    val serving_url: String,
    val sodium: String,
    val sugar: String,
    val vitamin_a: String,
    val vitamin_c: String
)
