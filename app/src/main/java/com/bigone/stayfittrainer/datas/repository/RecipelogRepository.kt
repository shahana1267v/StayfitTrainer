package com.bigOne.stayfittrainer.datas.repository

import android.app.Application
import android.util.Log
import com.bigOne.stayfittrainer.datas.model.AuthTokenResponse
import com.bigOne.stayfittrainer.remote.APIClient
import com.bigOne.stayfittrainer.datas.model.Recipe
import com.bigOne.stayfittrainer.datas.model.RecipeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipelogRepository (c: Application) {
    companion object {
        const val KEY_SHAREDPREFS = "sharedprefs_auth"
        const val KEY_AUTH_TOKEN = "dpm_auth_token"
    }

    private var mContext: Application = c
    private var didRestore = false

    private var _client: AuthTokenResponse? = null

    fun searchRecipe(query: String?): List<Recipe>? {
        try {
            // Make the API call using Retrofit
            val qrySpaceRemove = query?.replace("\\s".toRegex(), "")
                val response = APIClient.getApiServiceFatSecret(mContext).requestRecipeDetails(50,qrySpaceRemove).execute()
                return if (response.isSuccessful) {
                    Log.e("123", "successful response: ${response.body()?.recipes}")
                    response.body()?.recipes?.recipe
                } else {
                    // Log error message if the call is not successful
                    Log.e("124", "Unsuccessful response: ${response.code()}")
                    null
                }
        } catch (e: Exception) {
            // Log any exceptions that occur during the API call
            Log.e("123", e.toString())
            return null
        }
    }
    fun getRecipeDetails(id: String): RecipeData? {
        return try {
            // Make the API call using Retrofit
            val response = APIClient.getApiServiceFatSecret(mContext).requestFullRecipeDetails(id.toLong()).execute()
            if (response.isSuccessful) {
                Log.e("Log Exp 123", "successful response: ${ response.body()?.recipe}")
                response.body()?.recipe
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
            APIClient.getApiServiceFatSecret(mContext).getAuthToken().enqueue(object :
                Callback<AuthTokenResponse> {
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

    }


}


