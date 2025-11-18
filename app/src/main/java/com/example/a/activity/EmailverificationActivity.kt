package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R
import com.example.a.activity.ResetpasswordActivity

class EmailverificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailverification)

        val etCode = findViewById<EditText>(R.id.etCode)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val btnBack = findViewById<Button>(R.id.btnBack)  // 추가

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