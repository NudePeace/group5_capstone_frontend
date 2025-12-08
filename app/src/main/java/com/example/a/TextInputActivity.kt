package com.example.a

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TextInputActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var etSymptomInput: EditText
    private lateinit var btnNext: Button

    private var symptomText = "" // 증상 텍스트 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textinput)

        // UI 요소 연결
        btnBack = findViewById(R.id.btnBack)
        etSymptomInput = findViewById(R.id.etSymptomInput)
        btnNext = findViewById(R.id.btnNext)

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()
        }

        // EditText 최대 글자 수 설정 (500자)
        etSymptomInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE

        // 텍스트 변경 시 리스너
        etSymptomInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                symptomText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // 500자 초과 방지
                if (s != null && s.length > 500) {
                    s.delete(500, s.length)
                }
            }
        })

        // 다음 버튼
        btnNext.setOnClickListener {
            if (symptomText.trim().isNotEmpty()) {
                // 로딩 화면으로 이동 (LoadingActivity)
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra("symptom", symptomText) // 증상 데이터 전달
                startActivity(intent)
            } else {
                // 입력이 없는 경우 처리
                Toast.makeText(this, "증상을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}