package com.withpet.mobile.ui.activity.signup


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.data.repository.PetRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.databinding.ActivityPetInfoBinding
import com.withpet.mobile.ui.activity.MainActivity

class PetInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitPetInfo.setOnClickListener {
            val petSize = binding.etPetSize.text.toString()
            val petSex =
                if (binding.rgSexType.checkedRadioButtonId == binding.rbMale.id) "남자" else "여자"
            val petAge = binding.etPetAge.text.toString().toInt()
            val petIntroduction = binding.etPetIntroduction.text.toString()
            val memberId = binding.etMemberId.text.toString().toInt()

            binding.btnSubmitPetInfo.setOnClickListener {

                PetRepo.savePetInfo(petSize, petSex, petAge, petIntroduction, memberId,
                    success = {},
                    networkFail = {},
                    failure = {}
                )
            }
        }
    }

    // TODO : 회원가입 기존 로그인 소스(저장소에 저장까지) 옮기기 마무리하기
    // TODO : 1차 회원가입 응답값 인 id와 회원가입화면에서 받아온 아이디 비번값 Intent로 이 액티비티에 넘기기
    // TODO : 2차 테스트 해보고 완료 후 로그인 시키기
    // TODO : UI요소 버튼 validate 인풋 리스너 담시

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

}