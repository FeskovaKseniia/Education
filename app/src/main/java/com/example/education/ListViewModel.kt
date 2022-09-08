package com.example.education

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.education.data.search.SearchResponse
import com.example.education.repo.CryptoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Timed
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.*

class ListViewModel : ViewModel() {

    private val repo = CryptoRepository()

    val timerSubject: PublishSubject<String> = PublishSubject.create()

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

    fun startTimer() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS).timeInterval()
            .map(::timerMapper)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(timerSubject)
    }

    private fun timerMapper(time: Timed<Long>): String {
        val time = time.value().toString()
        Log.d("TIMER", time)
        return time
    }

    fun sendRequest(request: String): Observable<SearchResponse> {
        Log.d("SWITCH", request)
        return repo.search(request).toObservable()
    }
}