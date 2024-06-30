package com.withpet.mobile.data.managers

import com.withpet.mobile.data.enums.InputState

class InputStateManager(private val onStateChange: (InputState) -> Unit) {
    private var currentState: InputState = InputState.NAME_INPUT

    fun getCurrentState(): InputState = currentState

    fun nextState() {
        currentState = when (currentState) {
            InputState.NAME_INPUT -> InputState.AGE_INPUT
            InputState.AGE_INPUT -> InputState.GENDER_INPUT
            InputState.GENDER_INPUT -> InputState.EMAIL_INPUT
            InputState.EMAIL_INPUT -> InputState.PASSWORD_INPUT
            InputState.PASSWORD_INPUT -> InputState.PASSWORD_INPUT // 최종 상태에서 변경 없음
        }
        onStateChange(currentState)
    }
}
