package com.bigOne.StayFitTrainer.remote

import android.app.Application
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.bigOne.StayFitTrainer.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object APIClient {
    private var apiService: APIInterface? = null
    private lateinit var cachedToken: String

    fun getApiService(context: Application): APIInterface {
        return getApiServiceRepo(context)
    }

    class ErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (!response.isSuccessful) {
                val errorBody = response.body
                if (errorBody != null) {
                    val errorJson = errorBody.string()
                    val errorObject = JSONObject(errorJson)
                    val errorCode = errorObject.getJSONObject("error").getInt("code")
                    val errorMessage = errorObject.getJSONObject("error").getString("message")
                    throw HttpException(retrofit2.Response.error<Any>(errorCode, ResponseBody.create(null, errorMessage)))
                }
            }
            return response
        }
    }


        fun getApiServiceRepo(context: Application): APIInterface {
            val httpClient = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Set log level as desired
            }
            httpClient.addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("x-app-id", BuildConfig.NUTRITIONIX_APP_ID)
                    .addHeader("x-app-key", BuildConfig.NUTRITIONIX_APP_KEY)
                    .addHeader("x-remote-user-id", "0")
                    .build()
                chain.proceed(newRequest)
            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.NUTRITIONIX_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            apiService = retrofit.create(APIInterface::class.java)
            return apiService!!
        }

    fun getApiServiceFatSecret(context: Application): APIInterface {
        val httpClient = OkHttpClient.Builder()
        var baseUrl = "https://platform.fatsecret.com"
        val APP_URL = "https://platform.fatsecret.com/rest/server.api"
        val unixTime =  (System.currentTimeMillis() * 20).toString()
        httpClient.addInterceptor { chain ->
            val nonce = System.currentTimeMillis().toString()
            var url = chain.request().url.newBuilder()
                .addQueryParameter("format", "json")
                .addQueryParameter("oauth_consumer_key", "0f4153df6f8c4bc99b5b8150a6aa8b90")
                .addQueryParameter("oauth_nonce", nonce)
                .addQueryParameter("oauth_signature_method", "HMAC-SHA1")
                .addQueryParameter("oauth_timestamp", unixTime.toString())
                .addQueryParameter("oauth_version", "1.0")
                .addQueryParameter("region","IN")

            var build = url.build()
            val split = build.query?.split("&")?.toTypedArray()
            build = url.query(split?.let { paramify(it) }).build()

            val  base= chain.request().method+"&"+Uri.encode(APP_URL)+"&"+Uri.encode(build.query)
            val calc = sign(base)

            build = chain.request().url.newBuilder()
                .query(split?.let { paramify(it) })
                .addQueryParameter("oauth_signature",calc)
                .build()


            val newRequest = chain.request().newBuilder()
                .url(build)
                .build()
            chain.proceed(newRequest)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set log level as desired
        }
        httpClient.addInterceptor(ErrorInterceptor())
            .addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiService = retrofit.create(APIInterface::class.java)
        return apiService!!
    }
    }

    private fun sign(method: String): String? {
         val APP_SECRET = "64567ebb9065446aa9070b487492dbfe&"
        return try {
            val signingKey = SecretKeySpec(APP_SECRET.toByteArray(), "HmacSHA1")
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)
            Log.w("FatSecret_TEST DOne",String(Base64.encode(mac.doFinal(method.toByteArray()), Base64.DEFAULT)))
            String(Base64.encode(mac.doFinal(method.toByteArray()),Base64.DEFAULT)).trim()
        } catch (e: NoSuchAlgorithmException) {
            e.message?.let { Log.w("FatSecret_TEST FAIL", it) }
            null
        } catch (e: InvalidKeyException) {
            e.message?.let { Log.w("FatSecret_TEST FAIL", it) }
            null
        }
    }

    private fun paramify(params: Array<String>): String {
        val p = Arrays.copyOf(params, params.size)
        Arrays.sort(p)
        return join(p, "&")
    }

    private fun join(array: Array<String>, separator: String): String {
        val b = StringBuilder()
        for (i in array.indices) {
            if (i > 0)
                b.append(separator)
            b.append(array[i])
        }
        return b.toString()
    }