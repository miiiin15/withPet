package com.withpet.mobile.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.enums.IdCheckStatus
import com.withpet.mobile.data.enums.InputState
import com.withpet.mobile.databinding.ActivitySignupBinding
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.utils.ValidationUtils
import com.withpet.mobile.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupUI()
        setupListeners()

    }

    private fun setupObservers() {
        viewModel.inputState.observe(this) { state ->
            updateUI(state)
        }

        viewModel.isButtonEnabled.observe(this) { isEnabled ->
            binding.btnSignUp.isEnabled = isEnabled
        }

        viewModel.validationMessage.observe(this) { message ->
            message?.let { showAlert(it) }
        }

        viewModel.signupId.observe(this) { id ->
            if (id != null) {
                navigateToPetInfo(id)
            } else {
                showAlert("회원가입에 실패했습니다.")
            }
        }

        // TODO : 후속 액션 시점 확인하기
        viewModel.checkIdResult.observe(this) { result ->
            when (result.status) {
                IdCheckStatus.VALID -> showSnackBar(result.message) { successValidateID() }
                IdCheckStatus.INVALID -> showSnackBar(result.message)
                IdCheckStatus.FORMAT_ERROR -> showAlert(result.message)
                IdCheckStatus.ERROR -> showAlert(result.message)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI() {
        binding.selectGender.type = "gender"

        binding.btnSignUp.setOnClickListener {
            viewModel.validateAndProceed()
        }

        binding.btnCheckDuplication.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            viewModel.checkIdDuplication(loginId)
        }

        binding.outsideView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.outsideView.windowToken, 0)
                clearFocus()
            }
            true
        }
    }

    private fun setupListeners() {
        binding.etLoginId.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.EMAIL_INPUT)
                return ValidationUtils.isValidEmail(text)
            }
        })

        binding.etPassword.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.PASSWORD_INPUT)
                return ValidationUtils.isValidPassword(text)
            }
        })

        binding.etNickName.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.NAME_INPUT)
                return ValidationUtils.isValidUsername(text)
            }
        })

        binding.etAge.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                val ageText = if (text.isEmpty()) 0 else text.toInt()
                viewModel.validateInput(text, InputState.AGE_INPUT)
                return ValidationUtils.isValidAge(ageText)
            }
        })
    }

    private fun navigateToPetInfo(signupId: String) {
        val intent = Intent(this, PetInfoActivity::class.java).apply {
            putExtra("loginId", binding.etLoginId.text.toString())
            putExtra("password", binding.etPassword.text.toString())
            putExtra("signupId", signupId)
        }
        startActivity(intent)
        finish()
    }

    private fun clearFocus() {
        binding.etLoginId.clearFocus()
        binding.etPassword.clearFocus()
        binding.etNickName.clearFocus()
        binding.etAge.clearFocus()
    }

    // 이메일 검중 완료 후 동작
    private fun successValidateID() {
        binding.etLoginId.setDisable(true)
        binding.btnCheckDuplication.setEnable(false)
        viewModel.validateAndProceed()
    }

    private fun updateUI(state: InputState) {
        binding.btnSignUp.isEnabled = false
        when (state) {
            InputState.AGE_INPUT -> {
                binding.tvTitle.text = "나이를 입력해주세요"
                binding.etAge.visibility = View.VISIBLE
            }
            InputState.GENDER_INPUT -> {
                binding.btnSignUp.setEnable(true)
                binding.tvTitle.text = "성별을 입력해주세요"
                binding.selectGender.visibility = View.VISIBLE
            }
            InputState.EMAIL_INPUT -> {
                binding.tvTitle.text = "이메일을 입력해주세요"
                binding.layoutIdForm.visibility = View.VISIBLE
                binding.etLoginId.visibility = View.VISIBLE
                binding.btnCheckDuplication.visibility = View.VISIBLE
            }
            InputState.PASSWORD_INPUT -> {
                binding.tvTitle.text = "비밀번호를 입력해주세요"
                binding.etPassword.visibility = View.VISIBLE
            }
            else -> {}
        }
    }
}
