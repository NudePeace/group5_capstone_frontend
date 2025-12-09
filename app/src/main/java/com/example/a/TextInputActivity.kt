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

// CÁC IMPORT CẦN THIẾT CHO API
import android.util.Log
import com.example.a.model.AnalyzeResponse
import com.example.a.model.TextRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextInputActivity : AppCompatActivity() {

    private val apiService = ApiClient.service

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
            val text = symptomText.trim()
            if (text.isNotEmpty()) {
                val loadingIntent = Intent(this, LoadingActivity::class.java)
                startActivity(loadingIntent)

                sendSymptomText(text)

            } else {
                Toast.makeText(this, "증상을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSymptomText(text: String) {
        val request = TextRequest(text)

        apiService.analyzeUsingText(request).enqueue(object : Callback<AnalyzeResponse> {
            override fun onResponse(call: Call<AnalyzeResponse>, response: Response<AnalyzeResponse>) {
                if (response.isSuccessful) {
                    val chatResult = response.body()?.result
                    if (!chatResult.isNullOrEmpty()) {
                        Log.i("TextInputAPI", "API Success. Result: $chatResult")
                        val resultIntent = Intent(this@TextInputActivity, ResultActivity::class.java)
                        resultIntent.putExtra("chat_result", chatResult)
                        // Dọn stack
                        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(resultIntent)
                        finish()
                    } else {
                        Toast.makeText(this@TextInputActivity, "응답 오류.", Toast.LENGTH_LONG).show()
                        Log.e("TextInputAPI", "API failed: Empty result.")
                    }
                } else {
                    Toast.makeText(this@TextInputActivity, "서버 요청 실패: ${response.code()}", Toast.LENGTH_LONG).show()
                    Log.e("TextInputAPI", "API failed: HTTP ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                Log.e("TextInputAPI", "Network error", t)
                Toast.makeText(this@TextInputActivity, "네트워크 오류", Toast.LENGTH_LONG).show()
            }
        })
    }
}