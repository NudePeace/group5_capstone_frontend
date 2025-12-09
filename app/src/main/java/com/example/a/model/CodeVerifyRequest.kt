package com.example.a.model

import com.google.gson.annotations.SerializedName

data class CodeVerifyRequest(
    val email: String,
    @SerializedName("code")
    val code: String
)
