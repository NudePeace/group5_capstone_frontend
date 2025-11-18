package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R
import com.example.a.activity.SigninActivity

class LanguageActivity : AppCompatActivity() {

    private lateinit var btnKorean: Button
    private lateinit var btnEnglish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // UI 요소 초기화
        btnKorean = findViewById(R.id.btn_korean)
        btnEnglish = findViewById(R.id.btn_english)

        // 한국어 버튼 클릭
        btnKorean.setOnClickListener {
            selectLanguage("ko")
        }

        // English 버튼 클릭
        btnEnglish.setOnClickListener {
            selectLanguage("en")
        }
    }

    private fun selectLanguage(languageCode: String) {
        // 언어 설정 저장 (SharedPreferences 사용)
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("language", languageCode)
            apply()
        }

        // SigninActivity로 이동
        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
    }
}