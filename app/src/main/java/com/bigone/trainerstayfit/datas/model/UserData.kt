package com.bigOne.trainerstayfit.datas.model

data class UserData(

    val email: String,
    val id:String,
    val name:String,
    val img: String,
    var isTrainer: Boolean =false,
    val approved : Boolean=false,
    val qualification : String?=null,
    val experience : String?=null
)