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
    private lateinit var tvTitle: TextView
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
        tvTitle = findViewById(R.id.tvTitle)
        tvRecommendTitle = findViewById(R.id.tvRecommendTitle)
        tvRecommendTitleBold = findViewById(R.id.tvRecommendTitleBold)
        tvRecommendWarning = findViewById(R.id.tvRecommendWarning)
        tvAdditionalQuestion = findViewById(R.id.tvAdditionalQuestion)
        btnAsk = findViewById(R.id.btnAsk)
        btnFindHospital = findViewById(R.id.btnFindHospital)
        tvFindHospitalText = findViewById(R.id.tvFindHospitalText)

        // 4. 텍스트 세팅
        // 다국어 리소스를 사용하는 방식으로 전부 수정
        tvTitle.text = getString(R.string.result_title)
        tvRecommendTitle.text = getString(R.string.result_recommend_prefix)
        tvRecommendTitleBold.text = getString(R.string.result_recommend_suffix, department)
        tvRecommendWarning.text = getString(R.string.result_warning)
        tvAdditionalQuestion.text = getString(R.string.result_more_question)
        tvFindHospitalText.text = getString(R.string.result_find_hospital, department)

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
    // 언어 설정을 위해 getString 방식으로 바꿈
    private fun getDepartmentFromText(text: String): String {
        return when {
            text.contains("이") || text.contains("치아") || text.contains("잇몸") ->
                getString(R.string.dept_dental)

            text.contains("뼈") || text.contains("허리") ||
                    text.contains("다리") || text.contains("팔") ->
                getString(R.string.dept_orthopedic)

            text.contains("눈") ->
                getString(R.string.dept_eye)

            text.contains("귀") || text.contains("코") || text.contains("목") ->
                getString(R.string.dept_ent)

            text.contains("피부") ->
                getString(R.string.dept_dermatology)

            else ->
                getString(R.string.dept_internal)
        }
    }

}