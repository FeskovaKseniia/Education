package com.example.education.data

import com.example.education.data.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("coins/markets")
    suspend fun getCryptos(
        @Query("vs_currency") currency: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean
    ): Response<List<Crypto>>

    @GET("search")
    suspend fun searchCrypto(
        @Query("query") request: String
    ): Response<SearchResponse>
}
