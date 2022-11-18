package com.example.education

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.education.data.Crypto
import com.example.education.data.search.SearchResponse
import com.example.education.repo.CryptoRepository
import com.example.education.utils.ResultWrapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Timed
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.*

class ListViewModel : ViewModel() {

    private val repo = CryptoRepository()

    val timerSubject: PublishSubject<String> = PublishSubject.create()
    var loading = BehaviorSubject.create<Boolean>()
    var error = BehaviorSubject.create<String>()


    suspend fun getCryptoInfo(): List<SearchResponse> {
        loading.onNext(true)
        return withContext(viewModelScope.coroutineContext + exceptionHandler) {
            when (val response = repo.getCryptos()) {
                is ResultWrapper.NetworkError -> showNetworkError()
                is ResultWrapper.Error -> showError(response)
                is ResultWrapper.Success -> showSuccess(response.value)
            }
        }
    }

    private suspend fun showSuccess(value: Response<List<Crypto>>): List<SearchResponse> {
        return try {
            if (value.isSuccessful) {
                Log.d("COROUTINES", "getCryptos successful")
                return value.body()?.let { getListFromCryptos(it) }!!
            } else {
                Log.d("COROUTINES", "getCryptos unsuccessful")
                emptyList()
            }
        } catch (e: HttpException) {
            Log.d("COROUTINES", "getCryptos HttpException ${e.message()}")
            emptyList()
        } catch (e: Throwable) {
            Log.d("COROUTINES", "getCryptos Throwable ${e.localizedMessage}")
            emptyList()
        } finally {
            Log.d("COROUTINES", "Finish getCryptoInfo request")
            loading.onNext(false)
        }
    }

    private fun showNetworkError(): List<SearchResponse> {
        Log.d("COROUTINES", "getCryptos HttpException")
        loading.onNext(false)
        error.onNext("getCryptos HttpException")
        return emptyList()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        loading.onNext(false)
        Log.d("COROUTINES", message)
    }

    private fun showError(response: ResultWrapper.Error): List<SearchResponse> {
        onError(response.throwable.localizedMessage)
        loading.onNext(false)
        return emptyList()
    }

    private suspend fun getListFromCryptos(source: List<Crypto>): List<SearchResponse> {
        return withContext(Dispatchers.IO) {
            try {
                source.map {
                    async { repo.search(it.name).body() ?: SearchResponse() }
                }.awaitAll()
            } catch (throwable: Throwable) {
                Log.d("COROUTINES", "getListFromCryptos throwable ${throwable.localizedMessage}")
                emptyList()
            }
        }
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
}