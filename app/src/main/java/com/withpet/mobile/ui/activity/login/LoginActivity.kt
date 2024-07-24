package com.withpet.mobile.ui.activity.login

import android.content.Intent
import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityLoginBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.custom.IsValidListener

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setEnable(false)
        binding.btnSignIn.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(loginId, password)
        }

        setInputListener()
    }

    private fun setInputListener() {
        binding.etLoginId.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                setButtonEnable()
                // TODO : ID 규칙 추가하기
                return text.isNotEmpty()
            }
        })
        binding.etPassword.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                setButtonEnable()
                // TODO : 비밀번호 규칙 추가하기
                return text.isNotEmpty()
            }
        })

    }

    private fun setButtonEnable() {
        binding.btnSignIn.setEnable(binding.etLoginId.text!!.isNotEmpty() && binding.etPassword.text!!.isNotEmpty())
    }

    private fun signIn(loginId: String, password: String) {
        loadingDialog.show(supportFragmentManager, "")
        try {
            SignInRepo.signIn(
                loginId = loginId,
                password = password,
                networkFail = {
                    showAlert("로그인 네트워크 실패: $it")
                },
                success = {
                    if (it.payload == true) {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        showAlert("로그인 실패: ${it.result.message}")
                    }
                },
                failure = {
                    showAlert("로그인 에러: ${it.message}")
                }
            )
        } catch (e: Exception) {
        } finally {
            loadingDialog.dismiss()
        }
    }
}
