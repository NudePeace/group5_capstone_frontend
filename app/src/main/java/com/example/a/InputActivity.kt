package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class InputActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var btnTextInput: LinearLayout
    private lateinit var btnVoiceInput: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var ivTextIcon: ImageView
    private lateinit var ivVoiceIcon: ImageView
    private lateinit var tvTextLabel: TextView
    private lateinit var tvVoiceLabel: TextView

    private var selectedInputType = "" // 선택된 입력 방식 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // UI 요소 연결
        btnBack = findViewById(R.id.btnBack)
        btnTextInput = findViewById(R.id.btnTextInput)
        btnVoiceInput = findViewById(R.id.btnVoiceInput)
        btnNext = findViewById(R.id.btnNext)
        ivTextIcon = findViewById(R.id.ivTextIcon)
        ivVoiceIcon = findViewById(R.id.ivVoiceIcon)
        tvTextLabel = findViewById(R.id.tvTextLabel)
        tvVoiceLabel = findViewById(R.id.tvVoiceLabel)

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }

        // 텍스트 입력 선택
        btnTextInput.setOnClickListener {
            selectedInputType = "text"
            selectTextInput()
        }

        // 음성 입력 선택
        btnVoiceInput.setOnClickListener {
            selectedInputType = "voice"
            selectVoiceInput()
        }

        // 다음 버튼
        btnNext.setOnClickListener {
            when (selectedInputType) {
                "text" -> {
                    val intent = Intent(this, TextInputActivity::class.java)
                    startActivity(intent)
                }
                "voice" -> {
                    val intent = Intent(this, VoiceInputActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    // 선택하지 않은 경우 처리
                    Toast.makeText(this, "입력 방식을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 텍스트 입력 선택 상태 (전체 블록 회색)
    private fun selectTextInput() {
        // 텍스트 입력 선택 표시 (회색 배경)
        btnTextInput.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        ivTextIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        tvTextLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black))

        // 음성 입력 비선택 상태 (흰색)
        btnVoiceInput.setBackgroundResource(R.drawable.btn_border)
        ivVoiceIcon.clearColorFilter()
        tvVoiceLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }

    // 음성 입력 선택 상태 (전체 블록 회색)
    private fun selectVoiceInput() {
        // 음성 입력 선택 표시 (회색 배경)
        btnVoiceInput.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        ivVoiceIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        tvVoiceLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black))

        // 텍스트 입력 비선택 상태 (흰색)
        btnTextInput.setBackgroundResource(R.drawable.btn_border)
        ivTextIcon.clearColorFilter()
        tvTextLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }
}