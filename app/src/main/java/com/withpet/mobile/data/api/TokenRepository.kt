package com.withpet.mobile.data.api

import android.content.Context

class TokenRepository(private val context: Context) {
    private val preferences = context.getSharedPreferences("dataPreferences", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        preferences.edit().putString("access-token", token).apply()
    }

    fun saveRefreshToken(token: String) {
        preferences.edit().putString("refresh-token", token).apply()
    }

    fun getAccessToken(): String? = preferences.getString("access-token", null)
    fun getRefreshToken(): String? = preferences.getString("refresh-token", null)
}