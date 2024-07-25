package com.withpet.mobile.utils

import java.util.regex.Pattern

object ValidationUtils {

    // payload 확인
    fun isSuccessfulPayload(payload: Any): Boolean {
        return payload == true
    }

    // 비밀번호 유효성 검사 (8자 이상 16자 이하, 하나 이상의 숫자 포함)
    fun isValidPassword(password: String): Boolean {
        return password.length in 8..16 && password.any { it.isDigit() }
    }

    // 이메일 유효성 검사
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)\$"
        )
        return emailPattern.matcher(email).matches()
    }

    // 유저 이름 유효성 검사 (2자 이상 8자 이하)
    fun isValidUsername(username: String): Boolean {
        return username.length in 2..8
    }

    // 나이 유효성 검사 (3부터 120 사이의 정수)
    fun isValidAge(age: Int): Boolean {
        return age in 3..120
    }

    // 소개글 유효성 검사 (1자 이상 150자 이하)
    fun isValidDescription(description: String): Boolean {
        return description.length in 1..150
    }



}