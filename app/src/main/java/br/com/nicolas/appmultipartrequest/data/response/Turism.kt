package br.com.nicolas.appmultipartrequest.data.response

import com.google.gson.annotations.SerializedName

data class Turism(
    @SerializedName("message")
    val message: String,
    @SerializedName("url")
    val url: String
)