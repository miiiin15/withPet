package com.withpet.mobile.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivitySignupBinding
import com.withpet.mobile.ui.custom.IsValidListener

class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 외부 뷰 터치 시 키보드 내리기와 포커스 해제
        binding.outsideView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.outsideView.windowToken, 0)
                binding.etLoginId.clearFocus()
                binding.etPassword.clearFocus()
                binding.etNickName.clearFocus()
                binding.etAge.clearFocus()
            }
            true
        }

        setListeners()
        setupSignUpButton()
        setupCheckDuplicationButton()
    }

    private fun setupSignUpButton() {
        validButton()
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
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SignInRepo.checkDuplicate(
                loginId = loginId,
                success = {
                    Toast.makeText(this, "중복 검사 ${it.payload}", Toast.LENGTH_SHORT).show()
                },
                failure = {
                    Toast.makeText(this, "중복 검사 실패 $it", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setListeners() {
        binding.etLoginId.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                validButton()
                return text.isNotEmpty()
            }
        })

        binding.etPassword.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                validButton()
                return text.isNotEmpty()
            }
        })

        binding.etNickName.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                validButton()
                return text.isNotEmpty()
            }
        })

        binding.etAge.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                validButton()
                return text.isNotEmpty()
            }
        })
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
                    val intent = Intent(this, PetInfoActivity::class.java)
                    intent.putExtra("loginId", loginId)
                    intent.putExtra("password", password)
                    intent.putExtra("signupId", it.payload.toString())

                    startActivity(intent)
                    finish()
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
                    Toast.makeText(this, "회원가입 및 로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        // 새로운 Activity가 시작되면서 기존의 Activity를 모두 종료하고 새 Activity를 최상단에 위치시킵니다.
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish() // 현재 Activity 종료
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun validButton() {
        val loginId = binding.etLoginId.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val nickName = binding.etNickName.text.toString().trim()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0

        binding.btnSignUp.setEnable(loginId.isNotEmpty() && password.isNotEmpty() && nickName.isNotEmpty() && age > 0)
    }
}
