package com.example.education.data

import com.google.gson.annotations.SerializedName

data class Roi(

    @SerializedName("times") var times: Double? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("percentage") var percentage: Double? = null

)