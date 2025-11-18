package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.activity.MainActivity
import com.example.a.R

class LoginActivity : AppCompatActivity() {

    private lateinit var btnBack: Button

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
                // 간단한 로그인 (테스트용)
                if (email == "test@example.com" && password == "123456") {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

                    // MainActivity로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // LoginActivity 종료
                } else {
                    Toast.makeText(this, "이메일 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
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