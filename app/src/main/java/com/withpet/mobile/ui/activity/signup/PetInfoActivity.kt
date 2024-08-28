package com.withpet.mobile.ui.activity.signup


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.data.repository.PetRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityPetInfoBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.ui.custom.RadioItem
import com.withpet.mobile.ui.custom.SelectItem
import com.withpet.mobile.utils.Logcat
import com.withpet.mobile.utils.SharedPreferencesUtil
import com.withpet.mobile.utils.ValidationUtils

class PetInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityPetInfoBinding
    private var fromSignUp: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater)
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



        checkEntry()
        setOptionss()
        setButtons()
        setInputListener()

        binding.ivProfile.setOnClickListener {
            showAlert("기능 개발중")
        }

    }

    private fun clearFocus() {
        binding.etPetAge.clearFocus()
        binding.etPetIntroduction.clearFocus()
    }


    private fun setOptionss() {
        binding.selectPetSize.setOptions(
            arrayOf(
                SelectItem("소형", "Small"),
                SelectItem("중형", "Medium"),
                SelectItem("대형", "Large"),
            ),
        )

        binding.rgSexType.setOptions(
            arrayOf(
                RadioItem("남아", "Male"),
                RadioItem("여아", "Female"),
            ),
        )
    }

    private fun setButtons() {


        binding.btnSubmitPetInfo.setEnable(false)
        binding.btnSubmitPetInfo.setOnClickListener {
            loadingDialog.show(supportFragmentManager, "")
            val petSize = binding.selectPetSize.getValue() ?: ""
            val petSex = binding.rgSexType.getValue() ?: ""
            val petAge = safeStringToInt(binding.etPetAge.text.toString())
            val petIntroduction = binding.etPetIntroduction.text.toString()

            val loginId = intent.getStringExtra("loginId") ?: ""
            val password = intent.getStringExtra("password") ?: ""
            val id = if (fromSignUp) intent.getStringExtra("signupId")?.toDouble()?.toInt()
                ?: 0 else intent.getIntExtra("memberInfoId", 0)
            val petAddRequest = PetAddRequest(
                size = petSize,
                sex = petSex,
                age = petAge,
                introduction = petIntroduction,
                memberId = id
            )

            PetRepo.savePetInfo(petAddRequest, null,
                success = {
                    if (it.result.code == 200) {
                        if (fromSignUp) {
                            logIn(loginId, password)
                        } else {
                            // MainActivity 유저 정보 조회 트리거 용 값 세팅
                            val resultIntent = Intent()
                            resultIntent.putExtra("infoUpdated", true)
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                    } else {
                        showAlert("반려견 정보 등록 실패")
                    }
                },
                networkFail = {
                    showAlert("networkFail: 반려견 정보 등록 실패")
                },
                failure = {
                    showAlert("failure: 반려견 정보 등록 실패")
                },
                finally = {
                    loadingDialog.dismiss()
                }
            )
        }
    }

    // TODO : profileImage 어떻게 쏴야하는지 확인하기
    private fun setInputListener() {
        binding.etPetAge.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                setButtonEnable()
                return text.isNotEmpty()
            }
        })
        binding.etPetIntroduction.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                setButtonEnable()
                return ValidationUtils.isValidDescription(text)
            }
        })


    }

    private fun setButtonEnable() {
        binding.btnSubmitPetInfo.setEnable(
            binding.selectPetSize.getValue()!!
                .isNotEmpty() && binding.etPetAge.text!!.isNotEmpty() && ValidationUtils.isValidDescription(
                binding.etPetIntroduction.text.toString()
            )
        )
    }

    // 진입 분기 판별
    private fun checkEntry() {
        val entry = intent.getStringExtra("entry")
        // TODO : 일반 프로필 수정 분기 추가 할 것 + 일반 진입 시 api 로 기존 정보 조회 로직도 구현할 것
        if (entry == "main") {
            fromSignUp = false
        }
    }

    private fun logIn(loginId: String, password: String) {
        SignInRepo.logIn(
            loginId = loginId,
            password = password,
            networkFail = {
                Toast.makeText(this, "네트워크 실패: $it", Toast.LENGTH_SHORT).show()
            },
            success = {
                if (it.result.code == 200) {
                    SharedPreferencesUtil.saveLoginInfo(this, loginId, password)
                    Toast.makeText(this, "회원가입 및 로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        // 새로운 Activity가 시작되면서 기존의 Activity를 모두 종료하고 새 Activity를 최상단에 위치시킵니다.
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish() // 현재 Activity 종료
                } else {
                    showAlert("실패: ${it.result.message}")
                }
            },
            failure = {
                showAlert("에러: ${it.message}")
            }
        )
    }

    private fun safeStringToInt(str: String?, defaultValue: Int = 0): Int {
        return str?.toIntOrNull() ?: defaultValue
    }
}