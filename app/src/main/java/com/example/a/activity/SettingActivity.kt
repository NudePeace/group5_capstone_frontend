package com.example.a.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R

class SettingActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
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

        //  RadioButton 클릭 불가능하게 설정
        rbKorean.isClickable = false
        rbEnglish.isClickable = false

        //  SharedPreferences에서 저장된 언어 로드
        val sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val savedLanguage = sharedPref.getString("language", "korean") // 기본값: 한국어

        // 저장된 언어 설정에 따라 RadioButton 체크
        if (savedLanguage == "korean") {
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

        // 한국어 LinearLayout 클릭 시
        llKorean.setOnClickListener {
            rbKorean.isChecked = true
            rbEnglish.isChecked = false
            // SharedPreferences에 저장
            sharedPref.edit().putString("language", "korean").apply()
            Toast.makeText(this, "한국어로 변경되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 영어 LinearLayout 클릭 시
        llEnglish.setOnClickListener {
            rbEnglish.isChecked = true
            rbKorean.isChecked = false
            // SharedPreferences에 저장
            sharedPref.edit().putString("language", "english").apply()
            Toast.makeText(this, "English has been selected.", Toast.LENGTH_SHORT).show()
        }
    }
}