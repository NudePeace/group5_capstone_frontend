package com.example.a

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var rbKorean: RadioButton
    private lateinit var rbEnglish: RadioButton
    private lateinit var llKorean: LinearLayout
    private lateinit var llEnglish: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // UI 요소 연결
        btnBack = findViewById(R.id.btnBack)
        rbKorean = findViewById(R.id.rbKorean)
        rbEnglish = findViewById(R.id.rbEnglish)
        llKorean = findViewById(R.id.llKorean)
        llEnglish = findViewById(R.id.llEnglish)

        // RadioButton 클릭 불가능하게 설정
        rbKorean.isClickable = false
        rbEnglish.isClickable = false

        // ✅ LanguageUtil에서 저장된 언어코드 가져오기 ("ko" / "en")
        val savedLanguageCode = LanguageUtil.getSavedLanguage(this)

        // ✅ 언어코드에 맞게 RadioButton 체크 (여기가 "ko"/"en" 이어야 함)
        if (savedLanguageCode == "ko") {
            rbKorean.isChecked = true
            rbEnglish.isChecked = false
        } else {
            rbEnglish.isChecked = true
            rbKorean.isChecked = false
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }

        // ✅ 한국어 LinearLayout 클릭 시 한국어 설정을 변환
        llKorean.setOnClickListener {
            if (!rbKorean.isChecked) {  // 이미 선택된 상태면 중복 처리 방지
                rbKorean.isChecked = true
                rbEnglish.isChecked = false

                // 언어코드 "ko"로 저장
                LanguageUtil.saveLanguage(this, "ko")

                Toast.makeText(this, "한국어로 변경되었습니다.", Toast.LENGTH_SHORT).show()

                restartToMain()
            }
        }

        // 영어 LinearLayout 클릭 시 영어 설정으로 변환
        llEnglish.setOnClickListener {
            if (!rbEnglish.isChecked) {
                rbEnglish.isChecked = true
                rbKorean.isChecked = false

                // 언어코드 "en"으로 저장
                LanguageUtil.saveLanguage(this, "en")

                Toast.makeText(this, "English has been selected.", Toast.LENGTH_SHORT).show()

                // ✅ 메인 화면을 새로 띄우면서 스택 정리
                restartToMain()
            }
        }
    }

    // 메인으로 다시 시작하는 함수
    private fun restartToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // 설정 화면 종료
    }
}