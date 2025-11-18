package com.example.a

import com.example.a.model.EmailValidationResponse
import com.example.a.model.SignupResponse
import com.example.a.model.SignupRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
    @POST("user/signup")
    fun registerUser(@Body request: SignupRequest): Call<SignupResponse>
    @GET("/user/email-validation")
    fun checkEmailExists(@Query("email") email: String): Call<EmailValidationResponse>
}