package com.bigOne.trainerstayfit.remote


import com.bigOne.trainerstayfit.datas.model.AuthTokenResponse
import com.bigOne.trainerstayfit.datas.model.FoodDetailsData
import com.bigOne.trainerstayfit.datas.model.FoodsSearchResponse
import com.bigOne.trainerstayfit.datas.model.RecipeDetails
import com.bigOne.trainerstayfit.datas.model.SuggesstionFood
import com.bigOne.trainerstayfit.datas.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {




        @FormUrlEncoded
        @Headers("Accept: application/json")
        @POST("/connect/token")
        fun getAuthToken(
                @Field("scope") scope: String = "basic",
                @Field("grant_type") grantType: String = "client_credentials",
        ): Call<AuthTokenResponse>
       /* @Headers("Accept: application/json")
        @GET("/rest/server.api/foods.autocomplete.v2")
        fun requestFoodsSearch(@Query("method") method: String, @Query("expression") searchExpresion: String,): Call<Any>*/

        @GET("/rest/server.api?method=foods.search")
        fun requestFoodsSearch(@Query("search_expression") searchExpresion: String,@Query("max_result") max_result:Int): Call<FoodsSearchResponse>

        @GET("/rest/server.api?method=foods.autocomplete.v2")
        fun requestFoodsAutoSearch(@Query("expression") searchExpresion: String,@Query("max_result") max_result:Int): Call<SuggesstionFood>

        @GET("/rest/server.api?method=food.get.v4")
        fun requestFoodDetails(@Query("food_id") id: Long): Call<FoodDetailsData>

        @GET("/rest/server.api?method=recipes.search.v3")
        fun requestRecipeDetails(@Query("max_result") max_result: Int, @Query("search_expression") searchExpresion: String?):Call<RecipeResponse>

        @GET("/rest/server.api?method=recipe.get.v2")
        fun requestFullRecipeDetails(@Query("recipe_id") id: Long):Call<RecipeDetails>

}

