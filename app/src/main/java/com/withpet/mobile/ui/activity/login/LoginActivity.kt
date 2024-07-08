package com.withpet.mobile.ui.activity.login

import android.content.Intent
import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityLoginBinding
import com.withpet.mobile.ui.activity.MainActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(loginId, password)
        }
    }

    private fun signIn(loginId: String, password: String) {
        SignInRepo.signIn(
            loginId = loginId,
            password = password,
            networkFail = {
                showAlert("로그인 네트워크 실패: $it")
            },
            success = {
                if (it.result.code == 200) {
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
    }
}
