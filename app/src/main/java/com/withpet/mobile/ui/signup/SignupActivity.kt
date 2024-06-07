package com.withpet.mobile.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.MainActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSignUpButton()
        setupCheckDuplicationButton()
    }

    private fun setupSignUpButton() {
        binding.btnSignUp.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            val password = binding.etPassword.text.toString()
            val nickName = binding.etNickName.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0
            val sexType = if (binding.rbMale.isChecked) "MALE" else "FEMALE"

            signUp(loginId, password, nickName, age, sexType)
        }
    }

    private fun setupCheckDuplicationButton() {
        binding.btnCheckDuplication.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            if (loginId.isEmpty()) {
                Toast.makeText(this, "Login ID를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO : 중복 검사 로직 추가
            // 예시로 토스트 메시지 출력
            Toast.makeText(this, "중복 검사 완료", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp(
        loginId: String,
        password: String,
        nickName: String,
        age: Int,
        sexType: String
    ) {
        SignInRepo.signUp(
            loginId = loginId,
            password = password,
            nickName = nickName,
            age = age,
            sexType = sexType,
            success = {
                if (it.result.code == 200) {
                    signIn(loginId, password)
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            networkFail = {
                Toast.makeText(this, "네트워크 실패: $it", Toast.LENGTH_SHORT).show()
            },
            failure = {
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun signIn(loginId: String, password: String) {
        SignInRepo.signIn(
            loginId = loginId,
            password = password,
            networkFail = {
                Toast.makeText(this, "네트워크 실패: $it", Toast.LENGTH_SHORT).show()
            },
            success = {
                if (it.result.code == 200) {
                    saveCredentials(loginId, password)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun saveCredentials(loginId: String, password: String) {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("loginId", loginId)
        editor.putString("password", password)
        editor.apply()
    }
}
