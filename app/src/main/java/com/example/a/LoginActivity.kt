package com.example.a

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a.model.LoginRequest
import com.example.a.model.LoginResponse
import com.example.a.model.parseErrorDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
        private val apiService = ApiClient.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnBack = findViewById(R.id.btnBack)

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()  // 이전 액티비티로 돌아감
        }

        //  XML의 뷰 연결
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        // 이메일 키보드 표시
        etEmail.post {
            // 1. etEmail에 포커스 요청
            etEmail.requestFocus()

            // 2. InputMethodManager를 사용하여 키보드 강제 표시
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etEmail, InputMethodManager.SHOW_IMPLICIT)
        }

        // 로그인 버튼 클릭 이벤트
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(email, password)

                apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        val loginResponse = response.body()
                        if (response.isSuccessful) {
                            if (loginResponse != null && loginResponse.success) {
                                Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

                                // MainActivity로 이동
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish() // LoginActivity 종료
                            }
                        } else {
                            val errorMessage = parseErrorDetail(
                                response.errorBody(),
                                "오류: ${response.code()}" // Thông báo dự phòng
                            )

                            val messageToDisplay = "로그인 실패: $errorMessage"
                            Toast.makeText(this@LoginActivity, messageToDisplay, Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_LONG).show()
                        Log.e("LoginActivity", "API Call Failed", t)
                    }
                })
            }
        }

        // "비밀번호를 잊으셨나요?" 클릭 이벤트
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "비밀번호 재설정 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, FindpasswordActivity::class.java)
            startActivity(intent)
        }
    }
}