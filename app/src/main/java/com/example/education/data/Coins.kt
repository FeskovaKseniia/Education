package com.example.education.data

import com.google.gson.annotations.SerializedName

data class Coins(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("symbol")
    var symbol: String? = null,
    @SerializedName("market_cap_rank")
    var marketCapRank: Int? = null,
    @SerializedName("thumb")
    var thumb: String? = null,
    @SerializedName("large")
    var large: String? = null
)
