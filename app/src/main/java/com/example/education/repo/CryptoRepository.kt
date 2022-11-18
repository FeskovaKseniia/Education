package com.example.education.repo

import android.util.Log
import com.example.education.data.ApiInterface
import com.example.education.data.Crypto
import com.example.education.data.RetrofitClient
import com.example.education.data.RetrofitClient.safeApiCall
import com.example.education.data.search.SearchResponse
import com.example.education.utils.ResultWrapper
import retrofit2.Response

class CryptoRepository {
    private val api: ApiInterface = RetrofitClient.getClient()

    suspend fun getCryptos(): ResultWrapper<Response<List<Crypto>>> {
        Log.d("COROUTINES", "getCryptos request")
        return safeApiCall {
            api.getCryptos("usd", "market_cap_desc", 6, 1, false)
        }
    }

    suspend fun search(request: String?): Response<SearchResponse> {
        Log.d("COROUTINES", "search request for$request")
        return api.searchCrypto(request ?: "ethereum")
    }
}