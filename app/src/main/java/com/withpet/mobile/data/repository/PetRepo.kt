package com.withpet.mobile.data.repository

import com.google.gson.Gson
import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.utils.Logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PetRepo {

    fun savePetInfo(
        petAddRequest: PetAddRequest,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {


//        val requestBody = Gson().toJson(requestData).toRequestBody("application/json".toMediaType())

        // NetworkService에서 Retrofit 인터페이스를 통해 회원가입 요청을 보냄
        NetworkService.getService().requestSavePetInfo(petAddRequest)
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