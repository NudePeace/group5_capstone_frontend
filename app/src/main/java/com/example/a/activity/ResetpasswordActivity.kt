package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R

class ResetpasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)

        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnComplete = findViewById<Button>(R.id.btnComplete)
        val btnBack = findViewById<Button>(R.id.btnBack)  // 추가

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
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        //뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }
    }
}