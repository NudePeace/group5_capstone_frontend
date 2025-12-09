package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.model.ErrorResponse
import com.example.a.model.PasswordResetConfirmRequest
import com.example.a.model.PasswordResetResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetpasswordActivity : AppCompatActivity() {

    private val apiService = ApiClient.ApiService
    private var userEmail: String? = null

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
        setContentView(R.layout.activity_resetpassword)

        userEmail = intent.getStringExtra("email")

        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnComplete = findViewById<Button>(R.id.btnComplete)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)  // 추가

        btnComplete.setOnClickListener {
            val newPw = etNewPassword.text.toString()
            val confirmPw = etConfirmPassword.text.toString()

            if (newPw.isEmpty() || confirmPw.isEmpty()) {
                Toast.makeText(this, "모든 칸을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPw != confirmPw) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "비밀번호가 변경되었습니다. (임시)", Toast.LENGTH_SHORT).show()
            // 로그인 페이지 (LoginActivity)로 돌아갑니다.
            // 모든 스택을 정리하고 LoginActivity를 새롭게 시작합니다.
            confirmPasswordReset(userEmail!!, newPw)
        }

        //뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun confirmPasswordReset(email: String, newPassword: String) {
        val request = PasswordResetConfirmRequest(email, newPassword)

        apiService.confirmNewPassword(request).enqueue(object : Callback<PasswordResetResponse> {
            override fun onResponse(call: Call<PasswordResetResponse>, response: Response<PasswordResetResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(this@ResetpasswordActivity, body?.message ?: "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@ResetpasswordActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = ""

                    if (errorBody != null) {
                        val parsedError = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorMessage = parsedError.detail
                    }

                    Toast.makeText(this@ResetpasswordActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {
                Toast.makeText(this@ResetpasswordActivity, "네트워크 오류. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }
        })
    }
}