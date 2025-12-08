package com.example.a
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import android.text.Editable

class SignupActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnCheckEmail: Button
    private lateinit var btnSignup: Button
    private lateinit var tvEmailStatus: TextView
    private lateinit var tvPasswordStatus: TextView

    private var isEmailChecked = false
    private val registeredEmails = mutableListOf<String>() // 실제로는 서버에서 받아옴

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // UI 요소 초기화
        btnBack = findViewById(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        btnCheckEmail = findViewById(R.id.btnCheckEmail)
        btnSignup = findViewById(R.id.btnSignup)
        tvEmailStatus = findViewById(R.id.tvEmailStatus)
        tvPasswordStatus = findViewById(R.id.tvPasswordStatus)

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            finish()  // 이전 액티비티로 돌아감
        }

        // 중복확인 버튼 클릭
        btnCheckEmail.setOnClickListener {
            checkEmailDuplicate()
        }

        // 비밀번호 입력시 실시간 일치 확인
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordMatch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordMatch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 가입하기 버튼 클릭
        btnSignup.setOnClickListener {
            signup()
        }
    }

    // 이메일 중복 확인
    private fun checkEmailDuplicate() {
        val email = etEmail.text.toString().trim()

        // 이메일 유효성 검사
        if (email.isEmpty()) {
            tvEmailStatus.text = "이메일을 입력해주세요"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
            isEmailChecked = false
            return
        }

        if (!email.contains("@") || !email.contains(".")) {
            tvEmailStatus.text = "올바른 이메일 형식이 아닙니다"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
            isEmailChecked = false
            return
        }

        // 중복 확인 (실제로는 서버 통신)
        if (registeredEmails.contains(email)) {
            tvEmailStatus.text = "이미 가입된 이메일입니다"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
            isEmailChecked = false
        } else {
            tvEmailStatus.text = "사용 가능한 이메일입니다 ✓"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_green_light))
            isEmailChecked = true
        }
    }

    // 비밀번호 일치 여부 확인
    private fun checkPasswordMatch() {
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()

        if (password.isEmpty() || passwordConfirm.isEmpty()) {
            tvPasswordStatus.text = ""
            return
        }

        if (password == passwordConfirm) {
            tvPasswordStatus.text = "비밀번호가 일치합니다 ✓"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_green_light))
        } else {
            tvPasswordStatus.text = "비밀번호가 일치하지 않습니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
        }
    }

    // 회원가입 처리
    private fun signup() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()

        // 유효성 검사
        if (!isEmailChecked) {
            tvEmailStatus.text = "이메일 중복확인을 해주세요"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
            return
        }

        if (password.isEmpty()) {
            tvPasswordStatus.text = "비밀번호를 입력해주세요"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
            return
        }

        if (password.length < 6) {
            tvPasswordStatus.text = "비밀번호는 6자 이상이어야 합니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
            return
        }

        if (password != passwordConfirm) {
            tvPasswordStatus.text = "비밀번호가 일치하지 않습니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
            return
        }

        // 회원가입 성공 (실제로는 서버에 저장)
        registeredEmails.add(email)

        // 화면 종료 또는 로그인 화면으로 이동
        android.widget.Toast.makeText(
            this,
            "회원가입이 완료되었습니다!",
            android.widget.Toast.LENGTH_SHORT
        ).show()

        finish() // 이전 화면으로 돌아가기
    }
}