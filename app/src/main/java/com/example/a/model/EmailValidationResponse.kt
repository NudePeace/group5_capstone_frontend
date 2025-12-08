package com.example.a.model

import com.google.gson.annotations.SerializedName

data class EmailValidationResponse(
    @SerializedName("available")
    val available: Boolean,
    val message: String
)
