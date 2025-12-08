package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LanguageActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }
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