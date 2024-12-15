package com.withpet.mobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.data.enums.IdCheckStatus
import com.withpet.mobile.data.enums.InputState
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: SignInRepo
) : BaseViewModel() {

    internal data class IdCheckResult(
        val status: IdCheckStatus,
        val message: String
    )

    private val _inputState = MutableLiveData<InputState>()
    val inputState: LiveData<InputState> get() = _inputState

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _checkIdResult = MutableLiveData<IdCheckResult>()
    internal val checkIdResult: LiveData<IdCheckResult> get() = _checkIdResult

    private val _signupId = MutableLiveData<String>()
    val signupId: LiveData<String> get() = _signupId

    private var nickName: String = ""
    private var age: Int = 0
    private var gender: String = ""
    private var loginId: String = ""
    private var password: String = ""

    init {
        _inputState.value = InputState.NAME_INPUT // 초기 상태 설정
    }

    fun validateInput(text: String, state: InputState) {
        when (state) {
            InputState.NAME_INPUT -> nickName = text
            InputState.AGE_INPUT -> age = text.toIntOrNull() ?: 0
            InputState.GENDER_INPUT -> gender = text
            InputState.EMAIL_INPUT -> loginId = text
            InputState.PASSWORD_INPUT -> password = text
            else -> {}
        }

        _isButtonEnabled.value = when (state) {
            InputState.NAME_INPUT -> ValidationUtils.isValidUsername(nickName)
            InputState.AGE_INPUT -> ValidationUtils.isValidUsername(nickName) && ValidationUtils.isValidAge(
                age
            )

            InputState.GENDER_INPUT -> ValidationUtils.isValidUsername(nickName) && ValidationUtils.isValidAge(
                age
            ) && gender.isNotBlank()

            InputState.EMAIL_INPUT -> ValidationUtils.isValidUsername(nickName) && ValidationUtils.isValidAge(
                age
            ) && ValidationUtils.isValidEmail(loginId)

            InputState.PASSWORD_INPUT -> ValidationUtils.isValidUsername(nickName) && ValidationUtils.isValidAge(
                age
            ) && ValidationUtils.isValidEmail(loginId) && ValidationUtils.isValidPassword(password)

            else -> false
        }
    }

    fun validateAndProceed() {
        if (_isButtonEnabled.value == true) {
            when (_inputState.value) {
                InputState.PASSWORD_INPUT -> {
                    // 모든 입력이 유효한 상태에서 최종 단계이므로 회원가입 실행
                    signUp()
                }

                else -> {
                    // 다음 단계로 진행
                    _inputState.value = when (_inputState.value) {
                        InputState.NAME_INPUT -> InputState.AGE_INPUT
                        InputState.AGE_INPUT -> InputState.GENDER_INPUT
                        InputState.GENDER_INPUT -> InputState.EMAIL_INPUT
                        InputState.EMAIL_INPUT -> InputState.PASSWORD_INPUT
                        else -> InputState.PASSWORD_INPUT
                    }
                }
            }
        } else {
            // 현재 단계에서 유효성 검사 실패 시 오류 메시지 설정
            _validationMessage.value = when (_inputState.value) {
                InputState.NAME_INPUT -> "이름을 입력해주세요"
                InputState.AGE_INPUT -> "나이를 올바르게 입력해주세요"
                InputState.GENDER_INPUT -> "성별을 선택해주세요"
                InputState.EMAIL_INPUT -> "이메일을 올바르게 입력해주세요"
                InputState.PASSWORD_INPUT -> "비밀번호를 입력해주세요"
                else -> "입력값을 확인해주세요"
            }
        }
    }


    fun checkIdDuplication(loginId: String) {
        if (!ValidationUtils.isValidEmail(loginId)) {
            _checkIdResult.value = IdCheckResult(IdCheckStatus.FORMAT_ERROR, "이메일 형식을 확인해주세요.")
            return
        }
        launchDataLoad {
            try {
                val result = repository.checkDuplicate(loginId)
                val idCheckResult = if (result.payload == false) {
                    IdCheckResult(IdCheckStatus.VALID, "사용 가능한 아이디입니다.")
                } else {
                    IdCheckResult(IdCheckStatus.INVALID, "중복된 아이디입니다.")
                }
                withContext(Dispatchers.Main) {
                    _checkIdResult.value = idCheckResult
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _checkIdResult.value = IdCheckResult(IdCheckStatus.ERROR, "중복 검사 실패: ${e.message}")
                }
            }
        }
    }


    private fun signUp() {
        // TODO : 회원가입은 되지만 후속처리가 백그라운드 스레드에서 나오기 때문에 오류가 발생함 매칭리스트와 비교헤서 api요청 템플릿 맞춰볼걸
        launchDataLoad {
            try {
                val result = repository.signUp(loginId, password, nickName, age, gender)
                if (result.result.code == 200) {
                    _signupId.value = result.payload.toString()  // 회원가입 성공 시 ID 업데이트
                } else {
                    _validationMessage.postValue("회원가입 실패: ${result.result.message}")
                }
            } catch (e: Exception) {
                _validationMessage.postValue("회원가입 중 오류 발생: ${e.message}")
            }
        }
    }
}


