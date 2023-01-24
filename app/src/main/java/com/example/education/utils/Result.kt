package com.example.education.utils

sealed class Result {

    data class Success<out T>(val data: T?) : Result()

    data class Error(val msg: String) : Result()

    data class Loading<out T>(val data: T?) : Result()
}