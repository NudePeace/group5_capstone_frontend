package com.example.a

import com.example.a.model.AnalyzeResponse
import com.example.a.model.CodeVerifyRequest
import com.example.a.model.CodeVerifyResponse
import com.example.a.model.EmailValidationResponse
import com.example.a.model.LoginRequest
import com.example.a.model.LoginResponse
import com.example.a.model.PasswordResetConfirmRequest
import com.example.a.model.PasswordResetRequest
import com.example.a.model.PasswordResetResponse
import com.example.a.model.SignupResponse
import com.example.a.model.SignupRequest
import com.example.a.model.TextRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    fun registerUser(@Body request: SignupRequest): Call<SignupResponse>
    @GET(value="auth/check-email")
    fun checkEmailExists(@Query("email") email: String): Call<EmailValidationResponse>
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @POST("auth/logout")
    fun logout()
    @POST("auth/change-password")
    fun changePassword()
    @POST("auth/password-reset/request")
    fun restPassword(@Body request: PasswordResetRequest): Call<PasswordResetResponse>
    @POST("auth/password-reset/verify-code")
    fun verifyCode(@Body request: CodeVerifyRequest): Call<CodeVerifyResponse>
    @POST("auth/password-reset/confirm")
    fun confirmNewPassword(@Body request: PasswordResetConfirmRequest): Call<PasswordResetResponse>
    @POST("api/chat/predict/text")
    fun analyzeUsingText(@Body request: TextRequest): Call<AnalyzeResponse>
}