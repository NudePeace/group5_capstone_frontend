package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R
import com.example.a.activity.SignupActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var btnSignup: Button
    private lateinit var btnGuest: Button
    private lateinit var btnLogin: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btnBack = findViewById(R.id.btnBack)
        btnSignup = findViewById(R.id.btn_signup)
        btnGuest = findViewById(R.id.btn_guest)
        btnLogin = findViewById(R.id.btn_login)

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()  // 이전 액티비티로 돌아감
        }

        // 회원가입 버튼
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // 로그인 없이 사용하기
        btnGuest.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}