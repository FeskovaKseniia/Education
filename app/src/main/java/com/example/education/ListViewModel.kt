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

    fun getCryptoInfo(): Single<List<SearchResponse>> {
        return repo.getCryptos()
            .flattenAsObservable { it }
            .flatMap {
                repo.search(it.name).toObservable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun mergeTwoSearchResponses(
        firstRequest: String,
        secondRequest: String
    ): Observable<SearchResponse> {
        val firstResponse = repo.search(firstRequest)
        val secondResponse = repo.search(secondRequest)

        return firstResponse.mergeWith(secondResponse)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}