package com.bigOne.trainerstayfit.datas.model

data class UserData(
    val sex: String,
    val dob: String,
    var weight: String,
    var height: String,
    val email: String,
    val id:String,
    val name:String,
    val img: String,
    var isTrainer: Boolean =false,
    val approved : Boolean=false
)