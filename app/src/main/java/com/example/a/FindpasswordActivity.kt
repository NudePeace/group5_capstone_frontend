package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.a.model.PasswordResetRequest
import com.example.a.model.PasswordResetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class FindpasswordActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private val apiService = ApiClient.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpassword)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnSendCode.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            requestPasswordReset(email)
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun requestPasswordReset(email: String) {
        val request = PasswordResetRequest(email)

        apiService.restPassword(request).enqueue(object : Callback<PasswordResetResponse> {
            override fun onResponse(call: Call<PasswordResetResponse>, response: Response<PasswordResetResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(this@FindpasswordActivity, body?.message ?: "인증번호가 발송되었습니다.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@FindpasswordActivity, EmailverificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@FindpasswordActivity, "요청 실패: (${response.code()})", Toast.LENGTH_LONG).show()
                    Log.e("FindPassword", "Reset request failed: HTTP ${response.code()}, Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {
                Toast.makeText(this@FindpasswordActivity, "네트워크 오류. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                Log.e("FindPassword", "API Call Failed (Reset Request)", t)
            }
        })
    }
}