package com.withpet.mobile.data.repository

import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.api.response.VersionPayload
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommonRepo {

    fun getVersion(
        networkFail: (String) -> Unit,
        success: (ApiResponse<VersionPayload>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        NetworkService.getService().getVersion()
            .enqueue(object : Callback<ApiResponse<VersionPayload>> {
                override fun onResponse(
                    call: Call<ApiResponse<VersionPayload>>,
                    response: Response<ApiResponse<VersionPayload>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<VersionPayload>>, t: Throwable) {
                    failure(t)
                }
            })
    }

    fun getMemberInfo(
        networkFail: (String) -> Unit,
        success: (ApiResponse<MemberInfo>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        NetworkService.getService().getMemberInfo()
            .enqueue(object : Callback<ApiResponse<MemberInfo>> {
                override fun onResponse(
                    call: Call<ApiResponse<MemberInfo>>,
                    response: Response<ApiResponse<MemberInfo>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<MemberInfo>>, t: Throwable) {
                    failure(t)
                }
            })
    }
}


