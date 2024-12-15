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
import com.withpet.mobile.presentation.viewmodel.SignupViewModel
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
            binding.signUpButton.isEnabled = isEnabled
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
        binding.genderSelect.type = "gender"

        binding.signUpButton.setOnClickListener {
            viewModel.validateAndProceed()
        }

        binding.checkDuplicationButton.setOnClickListener {
            val loginId = binding.loginIdInput.text.toString()
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
        binding.loginIdInput.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.EMAIL_INPUT)
                return ValidationUtils.isValidEmail(text)
            }
        })

        binding.passwordInput.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.PASSWORD_INPUT)
                return ValidationUtils.isValidPassword(text)
            }
        })

        binding.nickNameInput.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                viewModel.validateInput(text, InputState.NAME_INPUT)
                return ValidationUtils.isValidUsername(text)
            }
        })

        binding.ageInput.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                val ageText = if (text.isEmpty()) 0 else text.toInt()
                viewModel.validateInput(text, InputState.AGE_INPUT)
                return ValidationUtils.isValidAge(ageText)
            }
        })
    }

    private fun navigateToPetInfo(signupId: String) {
        val intent = Intent(this, PetInfoActivity::class.java).apply {
            putExtra("loginId", binding.loginIdInput.text.toString())
            putExtra("password", binding.passwordInput.text.toString())
            putExtra("signupId", signupId)
        }
        startActivity(intent)
        finish()
    }

    private fun clearFocus() {
        binding.loginIdInput.clearFocus()
        binding.passwordInput.clearFocus()
        binding.nickNameInput.clearFocus()
        binding.ageInput.clearFocus()
    }

    // 이메일 검중 완료 후 동작
    private fun successValidateID() {
        binding.loginIdInput.setDisable(true)
        binding.checkDuplicationButton.setEnable(false)
        viewModel.validateAndProceed()
    }

    private fun updateUI(state: InputState) {
        binding.signUpButton.isEnabled = false
        when (state) {
            InputState.AGE_INPUT -> {
                binding.titleTextView.text = "나이를 입력해주세요"
                binding.ageInput.visibility = View.VISIBLE
            }
            InputState.GENDER_INPUT -> {
                binding.signUpButton.setEnable(true)
                binding.titleTextView.text = "성별을 입력해주세요"
                binding.genderSelect.visibility = View.VISIBLE
            }
            InputState.EMAIL_INPUT -> {
                viewModel.validateInput(binding.genderSelect.getValue()!!, InputState.GENDER_INPUT)
                binding.titleTextView.text = "이메일을 입력해주세요"
                binding.idFormLayout.visibility = View.VISIBLE
                binding.loginIdInput.visibility = View.VISIBLE
                binding.checkDuplicationButton.visibility = View.VISIBLE
            }
            InputState.PASSWORD_INPUT -> {
                binding.titleTextView.text = "비밀번호를 입력해주세요"
                binding.passwordInput.visibility = View.VISIBLE
            }
            else -> {}
        }
    }
}
