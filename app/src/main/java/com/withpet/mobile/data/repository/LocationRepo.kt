package com.withpet.mobile.data.repository

import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LocationRepo {

    fun saveLocation(
        x: Double,
        y: Double,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {

        val jsonBody = "{\"x\": \"${x}\", \"y\": \"${y}\"}"
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        NetworkService.getService().saveLocationList(requestBody)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            success(data)
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

