package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FindpasswordActivity : AppCompatActivity() {

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

            // 우선은 바로 다음 화면으로 이동
            Toast.makeText(this, "임시로 인증번호를 전송했다고 가정합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EmailverificationActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }
}