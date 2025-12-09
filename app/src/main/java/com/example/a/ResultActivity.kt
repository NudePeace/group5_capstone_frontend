package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

    private var recommendedDepartment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val analysisResult = intent.getStringExtra("chat_result") ?: ""

        if (analysisResult.isEmpty()) {
            Toast.makeText(this, "분석 결과를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        recommendedDepartment = analysisResult

        // 4. 뷰 연결
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        tvRecommendTitle = findViewById(R.id.tvRecommendTitle)
        tvRecommendTitleBold = findViewById(R.id.tvRecommendTitleBold)
        tvRecommendWarning = findViewById(R.id.tvRecommendWarning)
        tvAdditionalQuestion = findViewById(R.id.tvAdditionalQuestion)
        btnAsk = findViewById(R.id.btnAsk)
        btnFindHospital = findViewById(R.id.btnFindHospital)
        tvFindHospitalText = findViewById(R.id.tvFindHospitalText)

        // 5. 텍스트 세팅
        tvTitle.text = getString(R.string.result_title)
        tvRecommendTitle.text = getString(R.string.result_recommend_prefix)

        tvRecommendTitleBold.text = getString(R.string.result_recommend_suffix, recommendedDepartment)

        tvRecommendWarning.text = getString(R.string.result_warning)
        tvAdditionalQuestion.text = getString(R.string.result_more_question)

        tvFindHospitalText.text = getString(R.string.result_find_hospital, recommendedDepartment)

        // 6. 뒤로가기
        btnBack.setOnClickListener {
            val intent = Intent(this, TextInputActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 7. 질문하기 버튼
        btnAsk.setOnClickListener {
            // TODO: 질문하기 기능 추가
        }

        // 8. 주변 병원 찾기 버튼 (MapActivity 사용 시)
        btnFindHospital.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("keyword", recommendedDepartment)
            startActivity(intent)
        }
    }

}