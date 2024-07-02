package com.withpet.mobile.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.enums.InputState
import com.withpet.mobile.data.managers.InputStateManager
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivitySignupBinding
import com.withpet.mobile.ui.custom.CustomSelect
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.ui.custom.Option

class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var inputStateManager: InputStateManager

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

        // TODO : 성별 입력 팝업 연결하기
        // TODO : 팝업 높이 고정하는 설정 만들기
        inputStateManager = InputStateManager(::onStateChange)

        setListeners()
        setupSignUpButton()
        setupCheckDuplicationButton()
        setSelect()
        updateUI(inputStateManager.getCurrentState())
    }

    private fun clearFocus() {
        binding.etLoginId.clearFocus()
        binding.etPassword.clearFocus()
        binding.etNickName.clearFocus()
        binding.etAge.clearFocus()
    }

    private fun setSelect(){
        val customSelect: CustomSelect = findViewById(R.id.customSelect)
        val options = arrayOf(
            Option("하나", "01", checked = true),
            Option("다섯", "05"),
            Option("일곱", "07"),
            Option("둘", "02"),
            Option("여덟", "08"),
            Option("열", "10"),
            Option("구십구", "99")
        )
        customSelect.setOptions(options)
    }

    private fun setupSignUpButton() {
        binding.btnSignUp.setEnable(false)

        binding.btnSignUp.setOnClickListener {
            when (inputStateManager.getCurrentState()) {
                InputState.NAME_INPUT -> {
                    val nickName = binding.etNickName.text.toString()
                    if (nickName.isEmpty()) {
                        Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.AGE_INPUT -> {
                    val age = binding.etAge.text.toString().toIntOrNull()
                    if (age == null || age <= 0) {
                        Toast.makeText(this, "나이를 올바르게 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.GENDER_INPUT -> {
                    inputStateManager.nextState()
                }

                InputState.EMAIL_INPUT -> {
                    val loginId = binding.etLoginId.text.toString()
                    if (loginId.isEmpty()) {
                        Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        inputStateManager.nextState()
                    }
                }

                InputState.PASSWORD_INPUT -> {
                    val password = binding.etPassword.text.toString()
                    if (password.isEmpty()) {
                        Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        val loginId = binding.etLoginId.text.toString()
                        val nickName = binding.etNickName.text.toString()
                        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
                        val sexType = if (binding.rbMale.isChecked) "MALE" else "FEMALE"

                        signUp(loginId, password, nickName, age, sexType)
                    }
                }
            }
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
                binding.btnSignUp.setEnable(text.isNotEmpty())
                return text.isNotEmpty()
            }
        })

        binding.etPassword.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                binding.btnSignUp.setEnable(text.isNotEmpty())
                return text.isNotEmpty()
            }
        })

        binding.etNickName.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                binding.btnSignUp.setEnable(text.isNotEmpty())
                return text.isNotEmpty()
            }
        })

        binding.etAge.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                binding.btnSignUp.setEnable(text.toInt() > 0)
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

    private fun onStateChange(newState: InputState) {
        updateUI(newState)
    }

    private fun updateUI(state: InputState) {
        binding.btnSignUp.setEnable(false)
        when (state) {
            InputState.AGE_INPUT -> {
                binding.tvTitle.text ="나이를\n입력해주세요"
                binding.etAge.visibility = View.VISIBLE
            }

            InputState.GENDER_INPUT -> {
                binding.btnSignUp.setEnable(true)
                binding.tvTitle.text ="성별을\n입력해주세요"
                binding.rgSexType.visibility = View.VISIBLE
            }

            InputState.EMAIL_INPUT -> {
                binding.tvTitle.text ="이메일을\n입력해주세요"
                binding.layoutIdForm.visibility = View.VISIBLE
                binding.etLoginId.visibility = View.VISIBLE
                binding.btnCheckDuplication.visibility = View.VISIBLE
            }

            InputState.PASSWORD_INPUT -> {
                binding.tvTitle.text ="비밀번호를\n입력해주세요"
                binding.etPassword.visibility = View.VISIBLE
            }

            else -> {}
        }
    }
}

