package com.example.education.data

import com.example.education.data.search.SearchResponse
import io.reactivex.rxjava3.core.Single
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

    @GET("search")
    fun searchCrypto(
        @Query("query") request: String
    ): Single<SearchResponse>
}
