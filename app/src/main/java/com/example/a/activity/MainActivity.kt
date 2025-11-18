package com.example.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.R
import com.example.a.activity.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tabHome: LinearLayout
    private lateinit var tabMap: LinearLayout
    private lateinit var tabConsult: LinearLayout
    private lateinit var tabSetting: LinearLayout  // ✅ 변수명 일관성
    private lateinit var btnSymptomInput: Button
    private lateinit var btnNotification: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 연결
        tabHome = findViewById(R.id.tabHome)
        tabMap = findViewById(R.id.tabMap)
        tabConsult = findViewById(R.id.tabConsult)
        tabSetting = findViewById(R.id.tabSetting)  // ✅ tabSettings가 아니라 tabSetting
        btnSymptomInput = findViewById(R.id.btnSymptomInput)
        btnNotification = findViewById(R.id.btnNotification)

        // 증상 입력하기 버튼
        btnSymptomInput.setOnClickListener {
            Toast.makeText(this, "증상 입력 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
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
        tabSetting.setOnClickListener {  // ✅ tabSetting 사용
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}