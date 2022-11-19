package com.example.education.data

import com.example.education.data.search.SearchResponse

sealed class Result {
    data class Error(val msg: String?) : Result()
    data class Success(val response: SearchResponse) : Result()
}