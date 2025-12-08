package com.example.a

import com.example.a.model.EmailValidationResponse
import com.example.a.model.LoginRequest
import com.example.a.model.LoginResponse
import com.example.a.model.SignupResponse
import com.example.a.model.SignupRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
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
    fun restPassword()
    @POST("auth/password-reset/confirm")
    fun confirmNewPassword()
}