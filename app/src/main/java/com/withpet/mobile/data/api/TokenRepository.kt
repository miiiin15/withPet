package com.withpet.mobile.data.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenRepository(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        "encryptedDataPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccessToken(token: String) {
        preferences.edit().putString("access-token", token).apply()
    }

    fun saveRefreshToken(token: String) {
        preferences.edit().putString("refresh-token", token).apply()
    }

    fun getAccessToken(): String? = preferences.getString("access-token", null)
    fun getRefreshToken(): String? = preferences.getString("refresh-token", null)
}