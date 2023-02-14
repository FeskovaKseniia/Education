package com.example.education.network

import com.example.education.data.Crypto
import com.example.education.data.SearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("coins/markets")
    fun getCryptos(
        @Query("vs_currency") currency: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean
    ): Single<List<Crypto>>

    @GET("coins/markets")
    suspend fun getCryptosCall(
        @Query("vs_currency") currency: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean
    ): Response<List<Crypto>>

    @GET("search")
    fun searchCrypto(
        @Query("query") request: String
    ): Single<SearchResponse>

    @GET("search")
    suspend fun searchCryptoResponse(
        @Query("query") request: String
    ): Response<SearchResponse>

}
