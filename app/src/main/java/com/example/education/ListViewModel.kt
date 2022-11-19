package com.example.education

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.education.data.Result
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
    val requestWithError: PublishSubject<Result> = PublishSubject.create()

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

    private fun timerMapper(timed: Timed<Long>): String {
        val time = timed.value().toString()
        Log.d("TIMER", time)
        return time
    }

    fun startRequestWithError() {
        repo.search(WRONG_REQUEST)
            .doOnError { error ->
                requestWithError.onNext(Result.Error(error.localizedMessage))
            }
            .doOnSuccess { response ->
                if (response.coins.isEmpty()) {
                    requestWithError.onNext(Result.Error("is empty response error"))
                    requestWithError.onComplete()
                } else {
                    requestWithError.onNext(Result.Success(response))
                }
            }
            .onErrorReturn {
                Log.e("Wrong_request", it.localizedMessage ?: "some sort of error")
                SearchResponse()
            }
            .subscribeOn(Schedulers.io())
            .toObservable()
            .publish()
            .connect()
    }

    companion object {
        private const val WRONG_REQUEST = "betcoin"
        private const val CORRECT_REQUEST = "bitcoin"
    }
}