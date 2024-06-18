package com.withpet.mobile.data.repository

import com.google.gson.Gson
import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PetRepo {

    fun savePetInfo(
        size: String,
        sex: String,
        age: Int,
        introduction: String,
        memberId: Int,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        // 요청 데이터 생성
        val requestData = mapOf(
            "size" to size,
            "sex:" to sex,
            "age" to age,
            "introduction" to introduction,
            "memberId" to memberId
        )

        val requestBody = Gson().toJson(requestData).toRequestBody("application/json".toMediaType())

        // NetworkService에서 Retrofit 인터페이스를 통해 회원가입 요청을 보냄
        NetworkService.getService().requestSavePetInfo(requestBody)
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
}