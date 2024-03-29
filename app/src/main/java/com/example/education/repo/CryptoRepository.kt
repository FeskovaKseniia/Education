package com.example.education.repo

import com.example.education.network.ApiInterface
import com.example.education.data.Crypto
import com.example.education.network.RetrofitClient
import com.example.education.data.SearchResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CryptoRepository {
    private val api: ApiInterface = RetrofitClient.getClient()

    fun getCryptos(): Single<List<Crypto>> {
        return api.getCryptos("usd", "market_cap_desc", 6, 1, false)
            .subscribeOn(Schedulers.io())
    }

    fun search(request: String?): Single<SearchResponse> {
        return api.searchCrypto(request ?: "ethereum").subscribeOn(Schedulers.io())
    }
}