package com.example.a
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.a.model.EmailValidationResponse
import com.example.a.model.SignupRequest
import com.example.a.model.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private val apiService = ApiClient.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnBack = findViewById(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        btnCheckEmail = findViewById(R.id.btnCheckEmail)
        btnSignup = findViewById(R.id.btnSignup)
        tvEmailStatus = findViewById(R.id.tvEmailStatus)
        tvPasswordStatus = findViewById(R.id.tvPasswordStatus)

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

    private fun checkEmailDuplicate() {
        val email = etEmail.text.toString().trim()
        val redColor = getColor(android.R.color.holo_red_light)
        val greenColor = getColor(android.R.color.holo_green_light)

        // 이메일 유효성 검사
        if (email.isEmpty()) {
            tvEmailStatus.text = "이메일을 입력해주세요"
            tvEmailStatus.setTextColor(redColor)
            isEmailChecked = false
            return
        }

        if (!email.contains("@") || !email.contains(".")) {
            tvEmailStatus.text = "올바른 이메일 형식이 아닙니다"
            tvEmailStatus.setTextColor(redColor)
            isEmailChecked = false
            return
        }

        apiService.checkEmailExists(email).enqueue(object : Callback<EmailValidationResponse> {
            override fun onResponse(call: Call<EmailValidationResponse>, response: Response<EmailValidationResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.available) {
                            tvEmailStatus.text = "사용 가능한 이메일입니다 ✓"
                            tvEmailStatus.setTextColor(greenColor)
                            isEmailChecked = true
                        } else {
                            tvEmailStatus.text = body.message
                            tvEmailStatus.setTextColor(redColor)
                            isEmailChecked = false
                        }
                    } else {
                        tvEmailStatus.text = "서버 응답"
                        tvEmailStatus.setTextColor(redColor)
                        isEmailChecked = false
                        Log.e("SignupActivity", "Email check failed: Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    tvEmailStatus.text = "중복확인 실패: (${response.code()})"
                    tvEmailStatus.setTextColor(redColor)
                    isEmailChecked = false
                    Log.e("SignupActivity", "Email check failed: HTTP ${response.code()}, Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<EmailValidationResponse>, t: Throwable) {
                tvEmailStatus.text = "네트워크 오류. 다시 시도해주세요."
                tvEmailStatus.setTextColor(redColor)
                isEmailChecked = false
                Toast.makeText(this@SignupActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("SignupActivity", "API Call Failed (Email Check)", t)
            }
        })
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
            tvPasswordStatus.text = "비밀번호가 일치합니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_green_light))
        } else {
            tvPasswordStatus.text = "비밀번호가 일치하지 않습니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
        }
    }

    private fun signup() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()
        val redColor = getColor(android.R.color.holo_red_light)

        // 유효성 검사
        if (!isEmailChecked) {
            tvEmailStatus.text = "이메일 중복확인을 해주세요"
            tvEmailStatus.setTextColor(redColor)
            return
        }

        if (password.isEmpty()) {
            tvPasswordStatus.text = "비밀번호를 입력해주세요"
            tvPasswordStatus.setTextColor(redColor)
            return
        }

        if (password.length < 6) {
            tvPasswordStatus.text = "비밀번호는 6자 이상이어야 합니다"
            tvPasswordStatus.setTextColor(redColor)
            return
        }

        if (password != passwordConfirm) {
            tvPasswordStatus.text = "비밀번호가 일치하지 않습니다"
            tvPasswordStatus.setTextColor(redColor)
            return
        }

        val signupRequest = SignupRequest(email, password)

        apiService.registerUser(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    Toast.makeText(
                        this@SignupActivity,
                        signupResponse?.message ?: "회원가입이 완료되었습니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = "회원가입 실패: (${response.code()})"

                    Toast.makeText(this@SignupActivity, message, Toast.LENGTH_LONG).show()
                    Log.e("SignupActivity", "Registration failed: HTTP ${response.code()}, Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("SignupActivity", "API Call Failed (Registration)", t)
            }
        })
    }
}