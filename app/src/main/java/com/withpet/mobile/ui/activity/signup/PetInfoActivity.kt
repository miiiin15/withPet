package com.withpet.mobile.ui.activity.signup


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.data.repository.PetRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityPetInfoBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.custom.IsValidListener
import com.withpet.mobile.utils.Logcat
import com.withpet.mobile.utils.ValidationUtils

class PetInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetInfoBinding

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

        setButton()
        setInputListener()


    }

    private fun clearFocus() {
        binding.etPetAge.clearFocus()
        binding.etPetSize.clearFocus()
        binding.etPetIntroduction.clearFocus()
    }

    private fun setButton() {
        binding.btnSubmitPetInfo.setEnable(false)

        binding.btnSubmitPetInfo.setOnClickListener {
            val petSize = binding.etPetSize.text.toString()
            val petSex =
                if (binding.rgSexType.checkedRadioButtonId == binding.rbMale.id) "남자" else "여자"
            val petAge = safeStringToInt(binding.etPetAge.text.toString())
            val petIntroduction = binding.etPetIntroduction.text.toString()

            val loginId = intent.getStringExtra("loginId") ?: ""
            val password = intent.getStringExtra("password") ?: ""
            val signupId = intent.getStringExtra("signupId")?.toDouble()?.toInt() ?: 0

            val petAddRequest = PetAddRequest(
                size = petSize,
                sex = petSex,
                age = petAge,
                introduction = petIntroduction,
                memberId = signupId
            )
            PetRepo.savePetInfo(petAddRequest, null,
                success = {
                    if (it.result.code == 200) {
                        signIn(loginId, password)
                    } else {
                        Logcat.d("else")
                    }
                },
                networkFail = {
                    Logcat.d("networkFail")
                },
                failure = {
                    Logcat.d("failure")
                },
                finally = {}
            )
        }
    }

    // TODO : profileImage 어떻게 쏴야하는지 확인하기
    // TODO : UI요소 버튼 validate 인풋 리스너 담시
    private fun setInputListener() {
        binding.etPetSize.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                setButtonEnable()
                return text.isNotEmpty()
            }
        })
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
            binding.etPetSize.text!!.isNotEmpty() && binding.etPetAge.text!!.isNotEmpty() && ValidationUtils.isValidDescription(
                binding.etPetIntroduction.text.toString()
            )
        )
    }


    // TODO : 메인화면으로 넘긴후 분기처리 작업하기

    private fun signIn(loginId: String, password: String) {
        SignInRepo.signIn(
            loginId = loginId,
            password = password,
            networkFail = {
                Toast.makeText(this, "네트워크 실패: $it", Toast.LENGTH_SHORT).show()
            },
            success = {
                if (it.result.code == 200) {
                    saveCredentials(loginId, password)
                    Toast.makeText(this, "회원가입 및 로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        // 새로운 Activity가 시작되면서 기존의 Activity를 모두 종료하고 새 Activity를 최상단에 위치시킵니다.
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish() // 현재 Activity 종료
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun saveCredentials(loginId: String, password: String) {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("loginId", loginId)
        editor.putString("password", password)
        editor.apply()
    }

    private fun safeStringToInt(str: String?, defaultValue: Int = 0): Int {
        return str?.toIntOrNull() ?: defaultValue
    }
}