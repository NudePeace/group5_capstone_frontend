package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmailverificationActivity : AppCompatActivity() {

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

        val etCode = findViewById<EditText>(R.id.etCode)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val btnBack = findViewById<ImageButton>(R.id.btnBack) // 추가

        btnVerify.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(this, "인증번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "인증 완료 (임시)", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ResetpasswordActivity::class.java))
        }

        //뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }
}