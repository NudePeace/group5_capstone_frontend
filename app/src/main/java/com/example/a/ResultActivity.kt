package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var tvHeaderTitle: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvRecommendTitle: TextView
    private lateinit var tvRecommendTitleBold: TextView
    private lateinit var tvRecommendWarning: TextView
    private lateinit var tvAdditionalQuestion: TextView
    private lateinit var btnAsk: LinearLayout
    private lateinit var btnFindHospital: LinearLayout
    private lateinit var tvFindHospitalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // 1. LoadingActivity에서 받은 증상 텍스트
        val symptom = intent.getStringExtra("symptom") ?: ""

        // 2. JS의 키워드 분석 로직을 symptom 기준으로 변환
        val department = getDepartmentFromText(symptom)
        val shortSymptom = if (symptom.length > 10) {
            symptom.substring(0, 10) + "..."
        } else {
            symptom
        }

        // 3. 뷰 연결
        btnBack = findViewById(R.id.btnBack)
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)
        tvTitle = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        tvRecommendTitle = findViewById(R.id.tvRecommendTitle)
        tvRecommendTitleBold = findViewById(R.id.tvRecommendTitleBold)
        tvRecommendWarning = findViewById(R.id.tvRecommendWarning)
        tvAdditionalQuestion = findViewById(R.id.tvAdditionalQuestion)
        btnAsk = findViewById(R.id.btnAsk)
        btnFindHospital = findViewById(R.id.btnFindHospital)
        tvFindHospitalText = findViewById(R.id.tvFindHospitalText)

        // 4. 텍스트 세팅
        tvHeaderTitle.text = "분석 결과"
        tvTitle.text = "분석 결과"
        tvSubtitle.text = "'$shortSymptom' 증상이 예상됩니다."
        tvRecommendTitle.text = "사용자님의 증상에"
        tvRecommendTitleBold.text = "${department}를 추천합니다."
        tvRecommendWarning.text =
            "해당 추천은 일반적인 정보를 바탕으로 하며, 정확한 진단은 꼭 전문의와 상담하세요."
        tvAdditionalQuestion.text = "더 궁금하신 내용이 있나요?"
        tvFindHospitalText.text = "주변 $department 찾기"

        // 5. 뒤로가기
        btnBack.setOnClickListener {
            finish()
        }

        // 6. 질문하기 버튼 (추후 구현)
        btnAsk.setOnClickListener {
            // TODO: 질문하기 기능 추가
        }

        // 7. 주변 병원 찾기 버튼 (MapActivity 사용 시)
        btnFindHospital.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("keyword", department)
            startActivity(intent)
        }
    }

    // JS의 키워드 분석 로직을 Kotlin 함수로 변환
    private fun getDepartmentFromText(text: String): String {
        var dept = "내과"

        if (text.contains("이") || text.contains("치아") || text.contains("잇몸")) {
            dept = "치과"
        } else if (text.contains("뼈") || text.contains("허리") ||
            text.contains("다리") || text.contains("팔")
        ) {
            dept = "정형외과"
        } else if (text.contains("눈")) {
            dept = "안과"
        } else if (text.contains("귀") || text.contains("코") || text.contains("목")) {
            dept = "이비인후과"
        } else if (text.contains("피부")) {
            dept = "피부과"
        }

        return dept
    }
}