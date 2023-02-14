package com.example.education.data

import com.google.gson.annotations.SerializedName

data class Exchanges(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("market_type")
    var marketType: String? = null,
    @SerializedName("thumb")
    var thumb: String? = null,
    @SerializedName("large")
    var large: String? = null
)
