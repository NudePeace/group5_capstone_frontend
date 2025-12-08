package com.example.a.model

data class ErrorResponse(
    val detail: String
)
fun parseErrorDetail(errorBody: okhttp3.ResponseBody?, defaultMsg: String): String {
    if (errorBody == null) return defaultMsg
    return try {
        val gson = com.google.gson.Gson()
        val errorJsonString = errorBody.string()
        val parsedError = gson.fromJson(errorJsonString, ErrorResponse::class.java)
        parsedError.detail
    } catch (e: Exception) {
        defaultMsg
    }
}