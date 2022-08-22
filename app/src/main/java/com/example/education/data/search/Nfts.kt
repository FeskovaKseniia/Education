package com.example.education.data.search

import com.google.gson.annotations.SerializedName

data class Nfts(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("symbol")
    var symbol: String? = null,
    @SerializedName("thumb")
    var thumb: String? = null
)
