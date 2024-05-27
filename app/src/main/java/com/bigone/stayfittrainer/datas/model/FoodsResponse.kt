package com.bigOne.StayFitTrainer.datas.model

import com.google.gson.annotations.SerializedName

class FoodsResponse(
    @SerializedName("food") val foods: List<FoodFactResponse>,
    @SerializedName("max_results") val maxResults: Int,
    @SerializedName("total_results") val totalResult: Int,
    @SerializedName("page_number") val pageNumber: Int
)
