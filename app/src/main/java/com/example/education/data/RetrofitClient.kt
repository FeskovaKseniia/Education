package com.example.education.data

import com.example.education.utils.ResultWrapper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitClient {
    private var BASE_URL = "https://api.coingecko.com/api/v3/"

    fun getClient(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                else -> ResultWrapper.Error(throwable)
            }
        }
    }
}
