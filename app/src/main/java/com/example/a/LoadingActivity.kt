package com.example.a

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // 1. 이전 화면(TextInputActivity)에서 받은 증상 텍스트
        val symptom = intent.getStringExtra("symptom") ?: ""

        // 2. 뷰 연결
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        progressBar = findViewById(R.id.progressBar)

        tvTitle.text = "AI가 분석중입니다...\n잠시만 기다려주세요!"

        btnBack.setOnClickListener {
            finish()
        }

        // 3. 3초 뒤 ResultActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ResultActivity::class.java)
            // ResultActivity로도 symptom 그대로 전달
            intent.putExtra("symptom", symptom)
            startActivity(intent)
            finish() // 로딩 화면은 보통 닫아줌
        }, 3000)
    }
}