package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var tabHome: LinearLayout
    private lateinit var tabMap: LinearLayout
    private lateinit var tabConsult: LinearLayout
    private lateinit var tabSetting: LinearLayout
    private lateinit var btnSymptomInput: Button
    private lateinit var btnNotification: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 연결
        tabHome = findViewById(R.id.tabHome)
        tabMap = findViewById(R.id.tabMap)
        tabConsult = findViewById(R.id.tabConsult)
        tabSetting = findViewById(R.id.tabSetting)
        btnSymptomInput = findViewById(R.id.btnSymptomInput)
        btnNotification = findViewById(R.id.btnNotification)

        // 증상 입력하기 버튼 (InputActivity로 이동)
        btnSymptomInput.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        // 알림 버튼
        btnNotification.setOnClickListener {
            Toast.makeText(this, "알림 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        // 탭 클릭 리스너
        tabHome.setOnClickListener {
            Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show()
        }

        tabMap.setOnClickListener {
            Toast.makeText(this, "지도", Toast.LENGTH_SHORT).show()
        }

        tabConsult.setOnClickListener {
            Toast.makeText(this, "상담", Toast.LENGTH_SHORT).show()
        }

        // 설정 탭 클릭 리스너 (SettingActivity로 이동)
        tabSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}