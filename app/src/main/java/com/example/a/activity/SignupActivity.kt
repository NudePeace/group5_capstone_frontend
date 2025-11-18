package com.example.a.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.ApiClient
import com.example.a.R
import com.example.a.model.EmailValidationResponse
import com.example.a.model.SignupRequest
import com.example.a.model.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var btnBack: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnCheckEmail: Button
    private lateinit var btnSignup: Button
    private lateinit var tvEmailStatus: TextView
    private lateinit var tvPasswordStatus: TextView

    private var isEmailChecked = false
    private var isEmailAvailable = false
    private val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    private val emailPattern: Pattern = Pattern.compile(emailRegex)

    val passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}"
    private val passwordPattern: Pattern = Pattern.compile(passwordRegex)


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

        btnBack.setOnClickListener { finish() }
        btnSignup.setOnClickListener { signup() }
        btnCheckEmail.setOnClickListener { checkEmail() }

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isEmailChecked = false
                isEmailAvailable = false
                tvEmailStatus.text = ""
                updateSignupButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkPasswordMatch()
                updateSignupButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        updateSignupButtonState()
    }

    private fun checkEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty() || !emailPattern.matcher(email).matches()) {
            tvEmailStatus.text = "이메일 형식이 올바르지 않습니다"
            tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
            isEmailChecked = false
            isEmailAvailable = false
            updateSignupButtonState()
            return
        }

        tvEmailStatus.text = "중복 확인 중..."
        tvEmailStatus.setTextColor(getColor(android.R.color.darker_gray))
        btnCheckEmail.isEnabled = false

        ApiClient.userService.checkEmailExists(email).enqueue(object : Callback<EmailValidationResponse> {
            override fun onResponse(call: Call<EmailValidationResponse>, response: Response<EmailValidationResponse>) {
                btnCheckEmail.isEnabled = true
                isEmailChecked = true

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        isEmailAvailable = body.isAvailable
                        tvEmailStatus.text = body.message

                        val color = if (body.isAvailable) android.R.color.holo_green_light else android.R.color.holo_red_light
                        tvEmailStatus.setTextColor(getColor(color))

                    } else {
                        handleApiError("데이터 오류")
                    }
                } else {
                    handleApiError("API 오류: ${response.code()}")
                }
                updateSignupButtonState()
            }

            override fun onFailure(call: Call<EmailValidationResponse>, t: Throwable) {
                btnCheckEmail.isEnabled = true
                isEmailChecked = false
                isEmailAvailable = false
                handleApiError("네트워크 오류: 다시 시도해주세요")
                updateSignupButtonState()
            }
        })
    }

    private fun handleApiError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        tvEmailStatus.text = "오류 발생"
        tvEmailStatus.setTextColor(getColor(android.R.color.holo_red_light))
    }

    private fun checkPasswordMatch() {
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()

        if (password.isEmpty() || passwordConfirm.isEmpty()) {
            tvPasswordStatus.text = "비밀번호를 입력해주세요"
            tvPasswordStatus.setTextColor(getColor(android.R.color.darker_gray))
        } else if (!passwordPattern.matcher(password).matches()) {
            tvPasswordStatus.text = "비밀번호는 1개 이상의 숫자와 특수문자를 포함해야 합니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
        } else if (password != passwordConfirm) {
            tvPasswordStatus.text = "비밀번호가 일치하지 않습니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_red_light))
        } else {
            tvPasswordStatus.text = "비밀번호가 일치합니다"
            tvPasswordStatus.setTextColor(getColor(android.R.color.holo_green_dark))
        }
    }

    private fun updateSignupButtonState() {
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()
        val isPasswordValid = password.length >= 8 && password == passwordConfirm

        btnSignup.isEnabled = isEmailChecked && isEmailAvailable && isPasswordValid
    }

    // 회원가입 처리
    private fun signup() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (!isEmailChecked || !isEmailAvailable) {
            Toast.makeText(this, "이메일 중복확인 및 유효성 검사를 완료해주세요", Toast.LENGTH_LONG).show()
            return
        }
        if (password.length < 6 || password != etPasswordConfirm.text.toString()) {
            Toast.makeText(this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show()
            return
        }

        val requestBody = SignupRequest(email, password)

        ApiClient.userService.registerUser(requestBody).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "회원가입이 완료되었습니다! (Server)", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@SignupActivity, "회원가입 실패: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크 오류 발생", Toast.LENGTH_LONG).show()
            }
        })
    }
}