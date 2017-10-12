package com.wilderpereira.lmgtfygen.domain.repository.urlShortening

import com.google.gson.annotations.SerializedName

data class SearchUrl(
        val kind: String,
        @SerializedName("id") val shortUrl: String,
        val longUrl: String
)