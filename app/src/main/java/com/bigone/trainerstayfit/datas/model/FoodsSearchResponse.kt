package com.bigOne.trainerstayfit.datas.model

import com.google.gson.annotations.SerializedName

class FoodsSearchResponse(
    @SerializedName("foods") val foods: FoodsResponse
)
