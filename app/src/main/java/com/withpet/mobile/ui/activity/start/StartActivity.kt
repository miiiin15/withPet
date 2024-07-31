package com.withpet.mobile.ui.activity.start

import android.content.Intent
import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.databinding.ActivityStartBinding
import com.withpet.mobile.ui.activity.login.LoginActivity
import com.withpet.mobile.ui.activity.signup.SignupActivity
import com.withpet.mobile.ui.test.TestActivity

class StartActivity : BaseActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.buttonStart.setOnClickListener {
            handleButtonClick(ButtonType.START)
        }

        binding.buttonRegister.setOnClickListener {
            handleButtonClick(ButtonType.REGISTER)
        }

        binding.imgLogoName.setOnClickListener {
            handleButtonClick(ButtonType.LOGO)
        }
    }

    private fun handleButtonClick(buttonType: ButtonType) {
        val intent = when (buttonType) {
            ButtonType.START -> Intent(this, LoginActivity::class.java)
            ButtonType.REGISTER -> Intent(this, SignupActivity::class.java)
            ButtonType.LOGO -> Intent(this, TestActivity::class.java)
        }
        startActivity(intent)
    }

    private enum class ButtonType {
        START, REGISTER, LOGO
    }
}
