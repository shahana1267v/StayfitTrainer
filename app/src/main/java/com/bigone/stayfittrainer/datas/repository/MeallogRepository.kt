package com.bigOne.stayfittrainer.datas.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bigOne.stayfittrainer.datas.model.AuthTokenResponse

import com.bigOne.stayfittrainer.datas.model.FoodDetails
import com.bigOne.stayfittrainer.datas.model.FoodsResponse
import com.bigOne.stayfittrainer.remote.APIClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeallogRepository (c: Application) {
    companion object {
        const val KEY_SHAREDPREFS = "sharedprefs_auth"
         const val KEY_AUTH_TOKEN = "dpm_auth_token"
    }
    private var mContext: Application = c
    private var didRestore = false

    private var _client: AuthTokenResponse? = null


    init {
        restoreState()
    }


     fun searchMeal(query: String): FoodsResponse? {
        return try {
            // Make the API call using Retrofit
            val qrySpaceRemove=query.replace("\\s".toRegex(), "")
            val response = APIClient.getApiServiceFatSecret(mContext).requestFoodsSearch(qrySpaceRemove,50).execute()
            if (response.isSuccessful) {
                Log.e("Log Exp 123", "successful response: ${ response.body()?.foods}")
                response.body()?.foods
            } else {
                // Log error message if the call is not successful
                Log.e("Log Exp 124", "Unsuccessful response: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Log any exceptions that occur during the API call
            Log.e("Log Exp 123", e.toString())
            null
        }
    }
    fun searchAuto(query: String): FoodsResponse? {
        return try {
            // Make the API call using Retrofit
            val response = APIClient.getApiServiceFatSecret(mContext).requestFoodsSearch(query,50).execute()
            if (response.isSuccessful) {
                Log.e("Log Exp 123", "successful response: ${ response.body()?.foods}")
                response.body()?.foods
            } else {
                // Log error message if the call is not successful
                Log.e("Log Exp 124", "Unsuccessful response: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Log any exceptions that occur during the API call
            Log.e("Log Exp 123", e.toString())
            null
        }
    }
    fun getMealDetails(id: String): FoodDetails? {
        return try {
            // Make the API call using Retrofit
            val response = APIClient.getApiServiceFatSecret(mContext).requestFoodDetails(id.toLong()).execute()
            if (response.isSuccessful) {
                Log.e("Log Exp 123", "successful response: ${ response.body()?.foodDetails}")
                response.body()?.foodDetails
            } else {
                // Log error message if the call is not successful
                Log.e("Log Exp 124", "Unsuccessful response: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Log any exceptions that occur during the API call
            Log.e("Log Exp 123", e.toString())
            null
        }
    }

    fun getAPiKey() {
        try {
         APIClient.getApiServiceFatSecret(mContext).getAuthToken().enqueue(object : Callback<AuthTokenResponse> {
                    override fun onResponse(call: Call<AuthTokenResponse>, response: Response<AuthTokenResponse>) {
                        if (response.isSuccessful) {
                            val authToken = response.body()
                            setAccount(authToken)
                            Log.e("AuthTokenError", "Exception occurred: ${authToken?.token_type.toString()}")
                        }
                    }
                    override fun onFailure(call: Call<AuthTokenResponse>, t: Throwable) {
                        Log.e("AuthTokenError", "Exception occurred: ${t.message}")
                    }
                })
        } catch (e: Exception) {
            Log.e("AuthTokenError", "Exception occurred: ${e.message}")
        }
    }

    private fun setAccount(authToken: AuthTokenResponse?) {
        val authPrefs: SharedPreferences = mContext.getSharedPreferences(KEY_SHAREDPREFS, Context.MODE_PRIVATE)
        _client = authToken
        authPrefs.edit().putString(KEY_AUTH_TOKEN, Gson().toJson(authToken)).apply()

    }

    val client: AuthTokenResponse?
        get() {
            return _client
        }

    private fun restoreState() {
        if(didRestore) return
         didRestore = true
        val authPrefs: SharedPreferences = mContext.getSharedPreferences(KEY_SHAREDPREFS,Context.MODE_PRIVATE)
        val stateJson = authPrefs.getString(KEY_AUTH_TOKEN,null)
        if(stateJson!=null)
           _client = Gson().fromJson(stateJson,AuthTokenResponse::class.java)


    }




}