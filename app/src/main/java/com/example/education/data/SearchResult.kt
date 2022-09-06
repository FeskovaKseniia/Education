package com.example.education.data

import com.example.education.data.search.SearchResponse

sealed class SearchResult {
    data class Error(val throwable: Throwable?) : SearchResult()
    data class Success(val name: String?) : SearchResult()
}