package com.withpet.mobile.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.enums.InputState
import com.withpet.mobile.data.managers.InputStateManager
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivitySignupBinding
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.utils.ValidationUtils

class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var inputStateManager: InputStateManager
    private var isIdValid = false

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
                clearFocus()
            }
            true
        }

        inputStateManager = InputStateManager(::onStateChange)

        setListeners()
        binding.selectGender.type = "gender"
        setupSignUpButton()
        setupCheckDuplicationButton()
        updateUI(inputStateManager.getCurrentState())
    }

    private fun clearFocus() {
        binding.etLoginId.clearFocus()
        binding.etPassword.clearFocus()
        binding.etNickName.clearFocus()
        binding.etAge.clearFocus()
    }


    private fun setupSignUpButton() {
        binding.btnSignUp.setEnable(false)

        binding.btnSignUp.setOnClickListener {
            when (inputStateManager.getCurrentState()) {
                InputState.NAME_INPUT -> {
                    val nickName = binding.etNickName.text.toString()
                    if (nickName.isEmpty()) {
                        showAlert("이름을 입력해주세요")
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.AGE_INPUT -> {
                    val age = binding.etAge.text.toString().toIntOrNull()
                    if (age == null || age <= 0) {
                        showAlert("나이를 올바르게 입력해주세요")
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.GENDER_INPUT -> {
                    val gender = binding.selectGender.getValue()
                    if (gender.isNullOrBlank()) {
                        showAlert("성별을 선택해주세요")
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.EMAIL_INPUT -> {
                    val loginId = binding.etLoginId.text.toString()
                    if (loginId.isEmpty()) {
                        showAlert("이메일을 중복 확인 해주세요")
                    } else {
                        successValidateID()
                    }
                }

                InputState.PASSWORD_INPUT -> {
                    val password = binding.etPassword.text.toString()
                    if (password.isEmpty()) {
                        showAlert("비밀번호를 입력해주세요")
                    } else {
                        val loginId = binding.etLoginId.text.toString()
                        val nickName = binding.etNickName.text.toString()
                        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
                        val sexType = binding.selectGender.getValue() ?: ""

                        signUp(loginId, password, nickName, age, sexType)

                    }
                }
            }
        }
    }

    private fun setupCheckDuplicationButton() {
        binding.btnCheckDuplication.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            if (!ValidationUtils.isValidEmail(loginId)) {
                showAlert("이메일 형식을 확인해주세요.")
                return@setOnClickListener
            }
            SignInRepo.checkDuplicate(
                loginId = loginId,
                success = {
                    if (it.payload == false) {
                        isIdValid = true
                        binding.btnSignUp.setEnable(ValidationUtils.isValidEmail(loginId))
                        showSnackBar(message = "[${loginId}] 사용가능합니다",
                            buttonText = "사용",
                            onPress = {
                                successValidateID()
                            })
                    } else {
                        isIdValid = false
                        showSnackBar("중복된 아이디 입니다")
                    }
                },
                failure = {
                    showAlert("$it 검사 실패")
                }
            )
        }
    }

    private fun setListeners() {
        binding.etLoginId.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                isIdValid = false
                updateButtonState()
                return ValidationUtils.isValidEmail(text)
            }
        })

        binding.etPassword.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                updateButtonState()
                return ValidationUtils.isValidPassword(text)
            }
        })

        binding.etNickName.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                updateButtonState()
                return ValidationUtils.isValidUsername(text)
            }
        })

        binding.etAge.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                val ageText = if (text.isEmpty()) 0 else text.toInt()
                updateButtonState()
                return ValidationUtils.isValidAge(ageText)
            }
        })
    }

    private fun updateButtonState() {
        binding.btnSignUp.setEnable(validateCurrentState(inputStateManager.getCurrentState()))
    }

    private fun validateCurrentState(state: InputState): Boolean {

        val nickName = binding.etNickName.text.toString()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val loginId = binding.etLoginId.text.toString()
        val password = binding.etPassword.text.toString()

        return when (state) {
            InputState.NAME_INPUT ->
                ValidationUtils.isValidUsername(nickName)
            InputState.AGE_INPUT ->
                ValidationUtils.isValidUsername(nickName)
                        && ValidationUtils.isValidAge(age)
            InputState.GENDER_INPUT ->
                ValidationUtils.isValidUsername(nickName)
                        && ValidationUtils.isValidAge(age)
            InputState.EMAIL_INPUT ->
                ValidationUtils.isValidUsername(nickName)
                        && ValidationUtils.isValidAge(age)
                        && ValidationUtils.isValidEmail(loginId)
            InputState.PASSWORD_INPUT ->
                ValidationUtils.isValidUsername(nickName)
                        && ValidationUtils.isValidAge(age)
                        && ValidationUtils.isValidEmail(loginId)
                        && ValidationUtils.isValidPassword(password)
            else -> false
        }
    }

    // 이메일 검중 완료 후 동작
    private fun successValidateID() {
        inputStateManager.nextState()
        binding.etLoginId.setDisable(true)
        binding.btnCheckDuplication.setEnable(false)
    }

    private fun signUp(
        loginId: String,
        password: String,
        nickName: String,
        age: Int,
        sexType: String
    ) {
        try {
            loadingDialog.show(supportFragmentManager, "")
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
                        showAlert("실패: ${it.result.message}")
                    }
                },
                networkFail = {
                    showAlert("네트워크 실패: $it")
                },
                failure = {
                    showAlert("에러: ${it.message}")
                }
            )
        } catch (e: Exception) {
            showAlert("${e.message}")
        } finally {
            loadingDialog.dismiss()
        }
    }

    private fun onStateChange(newState: InputState) {
        updateUI(newState)
    }

    private fun updateUI(state: InputState) {
        binding.btnSignUp.setEnable(false)
        when (state) {
            InputState.AGE_INPUT -> {
                binding.tvTitle.text = "나이를\n입력해주세요"
                binding.etAge.visibility = View.VISIBLE
            }

            InputState.GENDER_INPUT -> {
                binding.btnSignUp.setEnable(true)
                binding.tvTitle.text = "성별을\n입력해주세요"
                binding.selectGender.visibility = View.VISIBLE
            }

            InputState.EMAIL_INPUT -> {
                binding.tvTitle.text = "이메일을\n입력해주세요"
                binding.layoutIdForm.visibility = View.VISIBLE
                binding.etLoginId.visibility = View.VISIBLE
                binding.btnCheckDuplication.visibility = View.VISIBLE
            }

            InputState.PASSWORD_INPUT -> {
                binding.tvTitle.text = "비밀번호를\n입력해주세요"
                binding.etPassword.visibility = View.VISIBLE
            }

            else -> {}
        }
    }
}

