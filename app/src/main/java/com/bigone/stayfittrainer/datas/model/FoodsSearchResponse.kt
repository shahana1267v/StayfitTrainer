package com.bigOne.stayfittrainer.datas.model

import com.google.gson.annotations.SerializedName

class FoodsSearchResponse(
    @SerializedName("foods") val foods: FoodsResponse
)
