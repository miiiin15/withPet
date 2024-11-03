package com.withpet.mobile.data.repository

import com.google.gson.Gson
import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.utils.DataProvider
import com.withpet.mobile.utils.Logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SignInRepo {

    suspend fun checkDuplicate(loginId: String): ApiResponse<Any> {
        return try {
            val response = NetworkService.getService().getCheckDuplicate(loginId).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                throw Exception("Network Error: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun signUp(
        loginId: String,
        password: String,
        nickName: String,
        age: Int,
        sexType: String
    ): ApiResponse<Any> {
        val requestData = mapOf(
            "loginId" to loginId,
            "password" to password,
            "nickName" to nickName,
            "age" to age,
            "sexType" to sexType
        )

        val requestBody = Gson().toJson(requestData).toRequestBody("application/json".toMediaType())

        return try {
            val response = NetworkService.getService().requestSignUp(requestBody).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                throw Exception("Network Error: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun logIn(loginId: String, password: String): ApiResponse<Any> {
        val jsonBody = "{\"loginId\": \"${loginId.trim()}\", \"password\": \"${password.trim()}\"}"
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        return try {
            val response = NetworkService.getService().requestSignIn(requestBody).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                throw Exception("Network Error: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
