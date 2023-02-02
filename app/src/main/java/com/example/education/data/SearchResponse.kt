package com.example.education.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("coins")
    var coins: ArrayList<Coins> = arrayListOf(),
    @SerializedName("exchanges")
    var exchanges: ArrayList<Exchanges> = arrayListOf(),
    @SerializedName("icos")
    var icos: ArrayList<String> = arrayListOf(),
    @SerializedName("categories")
    var categories: ArrayList<Categories> = arrayListOf(),
    @SerializedName("nfts")
    var nfts: ArrayList<Nfts> = arrayListOf()
)