package com.example.education.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.education.data.Crypto
import com.example.education.data.SearchResponse
import com.example.education.network.ApiInterface
import com.example.education.network.RetrofitClient
import com.example.education.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FlowViewModel : ViewModel() {

    private val api: ApiInterface = RetrofitClient.getClient()

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    fun incrementFlow() { _stateFlow.value += 1 }

    private val _sharedFlow = MutableSharedFlow<Int>(replay = 0)
    val sharedFlow = _sharedFlow.asSharedFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _cryptos = MutableStateFlow<Result>(Result.Loading<List<Crypto>>(null))
    val cryptos = _cryptos.asStateFlow()
    private val _crypto = MutableStateFlow<Result>(Result.Loading<SearchResponse>(null))
    val crypto = _crypto.asStateFlow()

    fun startRequestForOneCrypto() {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch(SupervisorJob()) {
            try {
                val response = api.getCryptosCall("usd", "market_cap_desc", 6, 1, false)
                if (response.isSuccessful) {
                    response.body()?.let { list ->
                        _cryptos.emit(Result.Success(response.body()))
                        launch {
                            try {
                                val response2 = api.getCryptosCall("ud", "market_cap_desc", 6, 1, false)
                                if (response2.isSuccessful) {
                                    _cryptos.emit(Result.Success(response2.body()))
                                } else {
                                    Log.e("ERROR", response.toString())
                                    _cryptos.emit(Result.Error("Error code: ${response2.code()},\nsee logs for \nmore information"))
                                }
                            } catch (throwable: Throwable) {
                                catchThrowable(throwable, _cryptos)
                            }
                        }
                    }
                } else {
                    Log.e("ERROR", response.toString())
                    _cryptos.emit(Result.Error("Error code: ${response.code()},\n see logs for \nmore information"))
                }
            } catch (throwable: Throwable) {
                catchThrowable(throwable, _cryptos)
            }
        }
    }

    private suspend fun catchThrowable(
        throwable: Throwable,
        sharedFlow: MutableSharedFlow<Result>? = null,
        stateFlow: MutableStateFlow<Result>? = null
    ) {
        when (throwable) {
            is IOException -> {
                sharedFlow?.emit(Result.Error(throwable.message ?: "IOException"))
                stateFlow?.emit(Result.Error(throwable.message ?: "IOException"))
            }
            is HttpException -> {
                sharedFlow?.emit(Result.Error("${throwable.code()}: ${throwable.message()}"))
                stateFlow?.emit(Result.Error("${throwable.code()}: ${throwable.message()}"))
            }
            else -> {
                sharedFlow?.emit(Result.Error(throwable.message ?: throwable.localizedMessage))
                stateFlow?.emit(Result.Error(throwable.message ?: throwable.localizedMessage))
            }
        }
    }

    suspend fun sharedFlow() { _sharedFlow.emit(_stateFlow.value) }
}