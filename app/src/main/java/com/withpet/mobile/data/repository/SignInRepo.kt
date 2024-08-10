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

    fun checkDuplicate(
        loginId: String,
        networkFail: (String) -> Unit = {},
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {

        NetworkService.getService().getCheckDuplicate(loginId)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                    failure(t)
                }
            })
    }

    fun signUp(
        loginId: String,
        password: String,
        nickName: String,
        age: Int,
        sexType: String,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        // 요청 데이터 생성
        val requestData = mapOf(
            "loginId" to loginId,
            "password" to password,
            "nickName" to nickName,
            "age" to age,
            "sexType" to sexType
        )

        val requestBody = Gson().toJson(requestData).toRequestBody("application/json".toMediaType())

        // NetworkService에서 Retrofit 인터페이스를 통해 회원가입 요청을 보냄
        NetworkService.getService().requestSignUp(requestBody)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                    failure(t)
                }
            })
    }

    fun logIn(
        loginId: String,
        password: String,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {

        val jsonBody = "{\"loginId\": \"${loginId.trim()}\", \"password\": \"${password.trim()}\"}"
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        NetworkService.getService().requestSignIn(requestBody)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            success(data)
                            DataProvider.isLogin = true
                        } else {
                            networkFail("Empty response body")
                        }
                    } else {
                        val data = response.errorBody()?.string() ?: return
                        networkFail(data)
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                    failure(t)
                }
            })
    }

}