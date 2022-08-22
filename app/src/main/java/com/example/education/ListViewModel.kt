package com.example.education

import androidx.lifecycle.ViewModel
import com.example.education.data.ApiInterface
import com.example.education.data.Crypto
import com.example.education.data.RetrofitClient
import com.example.education.data.search.SearchResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject

class ListViewModel : ViewModel() {

    private val api: ApiInterface = RetrofitClient.getClient()

    val cryptoList = AsyncSubject.create<List<Crypto>>()

    fun sendRequest(): Observable<List<Crypto>> {
        return api.getCryptos("usd", "market_cap_desc", 100, 1, false)
    }

    fun sendFirstRequest(): Observable<Int> {
        return Observable.fromArray(2, 3, 4, 5)
    }

    fun sendSecndRequest(): Observable<Int> {
        return Observable.fromArray(20, 30, 40).subscribeOn(Schedulers.io())
    }

    fun search(request: String): Observable<SearchResponse> {
        return api.searchCrypto(request)
    }
}