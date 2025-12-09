package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.model.CodeVerifyRequest
import com.example.a.model.CodeVerifyResponse
import com.example.a.model.ErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class EmailverificationActivity : AppCompatActivity() {

    private val apiService = ApiClient.ApiService
    private var email: String? = null

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailverification)

        email = intent.getStringExtra("email")

        val etCode = findViewById<EditText>(R.id.etCode)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val btnBack = findViewById<ImageButton>(R.id.btnBack) // 추가

        btnVerify.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(this, "인증번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verifyResetCode(email!!, code)
        }

        //뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun verifyResetCode(email: String, code: String) {
        val request = CodeVerifyRequest(email, code)

        apiService.verifyCode(request).enqueue(object : Callback<CodeVerifyResponse> {
            override fun onResponse(call: Call<CodeVerifyResponse>, response: Response<CodeVerifyResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(this@EmailverificationActivity, body?.message ?: "인증 성공!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@EmailverificationActivity, ResetpasswordActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = ""

                    if (errorBody != null) {
                        val parsedError = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorMessage = parsedError.detail
                    }

                    Toast.makeText(this@EmailverificationActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CodeVerifyResponse>, t: Throwable) {
                Toast.makeText(this@EmailverificationActivity, "네트워크 오류. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }
        })
    }

}