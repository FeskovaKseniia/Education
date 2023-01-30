package com.example.education.di.modules

import com.example.education.data.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.*
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApiModule {

    private const val API_DEFAULT_NAME = "ApiDefault"
    private var BASE_URL = "https://api.coingecko.com/api/v3/"

    @Provides
    @Singleton
    fun getApiService(@Named(API_DEFAULT_NAME) retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    @Named(API_DEFAULT_NAME)
    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}