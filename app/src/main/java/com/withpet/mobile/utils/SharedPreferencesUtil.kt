package com.withpet.mobile.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesUtil {

    private const val PREFERENCES_NAME = "userPreferences"

    // SharedPreferences 초기화
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // 로그인 정보 저장
    fun saveLoginInfo(context: Context, loginId: String, password: String) {
        val sharedPreferences = getPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("loginId", loginId)
        editor.putString("password", password)
        editor.apply()

        // 로그 출력
        Log.d("SharedPreferencesUtil", "로그인 정보 저장: loginId = $loginId")
    }

    // 저장된 로그인 정보 가져오기
    fun getLoginInfo(context: Context): Pair<String?, String?> {
        val sharedPreferences = getPreferences(context)
        val loginId = sharedPreferences.getString("loginId", null)
        val password = sharedPreferences.getString("password", null)

        // 로그 출력
        Log.d("SharedPreferencesUtil", "로그인 정보 가져오기: loginId = $loginId")

        return Pair(loginId, password)
    }

    // 로그인 정보 삭제
    fun clearLoginInfo(context: Context) {
        val sharedPreferences = getPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove("loginId")
        editor.remove("password")
        editor.apply()

        // 로그 출력
        Log.d("SharedPreferencesUtil", "로그인 정보 삭제")
    }
}
