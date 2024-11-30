package com.withpet.mobile.ui.activity.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityLoginBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.utils.SharedPreferencesUtil
import com.withpet.mobile.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setEnable(false)
        binding.btnSignIn.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.logIn(loginId, password) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                SharedPreferencesUtil.saveLoginInfo(this, loginId, password)
                startActivity(intent)
                finish()
            }
        }

        setInputListener()
        setViewModel()
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


    private fun setViewModel() {
        viewModel.error.observe(this) {
            showAlert(it)
        }
        viewModel.isLoading.observe(this){
            if (it){
                loadingDialog.show(supportFragmentManager, "")
            }else{
                loadingDialog.dismiss()
            }
        }
    }

    private fun setButtonEnable() {
        binding.btnSignIn.setEnable(binding.etLoginId.text!!.isNotEmpty() && binding.etPassword.text!!.isNotEmpty())
    }
}
