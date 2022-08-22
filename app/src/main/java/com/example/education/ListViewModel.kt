package com.example.education

import androidx.lifecycle.ViewModel
import com.example.education.data.search.SearchResponse
import com.example.education.repo.CryptoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val repo = CryptoRepository()

    fun sendRequest(): Single<SearchResponse> {
        return repo.getCryptos()
            .flatMap {
                repo.search(it[0].name)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun merge(): Observable<SearchResponse> {
        val firstObservable = repo.search("ethereum")
        val secondObservable = repo.search("bitcoin")

        return firstObservable.mergeWith(secondObservable)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toObservable()
    }
}