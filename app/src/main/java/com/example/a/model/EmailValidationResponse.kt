package com.example.a.model

import com.google.gson.annotations.SerializedName

data class EmailValidationResponse(
    @SerializedName("is_available")
    val isAvailable: Boolean,
    val message: String
)
