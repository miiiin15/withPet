package com.withpet.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.R
import com.withpet.mobile.databinding.ActivityLoginBinding
import com.withpet.mobile.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStartButton()
        setupRegisterButton()
    }


    private fun setupStartButton() {
        // TODO : 로그인 로직 처리
    }

    private fun setupRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

}
