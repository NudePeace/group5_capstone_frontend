package com.example.a.model

import com.google.gson.annotations.SerializedName

data class PasswordResetConfirmRequest(
    val email: String,
    @SerializedName("new_password")
    val newPassword: String
)
