package com.bigOne.stayfittrainer.datas.model

data class AuthTokenResponse(
    val access_token: String?,
    val token_type:String?,
    val expires_in: Int?
)
