package com.withpet.mobile.data.repository

import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.api.response.VersionPayload
import com.withpet.mobile.data.model.Someone
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
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

    // 메인 매칭 리스트 조회
    fun getMatchedList(
        networkFail: (String) -> Unit,
        success: (ApiResponse<List<Someone>>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        NetworkService.getService().getMatchingList()
            .enqueue(object : Callback<ApiResponse<List<Someone>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<Someone>>>,
                    response: Response<ApiResponse<List<Someone>>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<Someone>>>, t: Throwable) {
                    failure(t)
                }
            })
    }

    // 좋아요
    fun sendLike(
        likeProfileId : String,
        networkFail: (String) -> Unit ={},
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ){

        val jsonBody = "{\"loginId\": \"${likeProfileId}\"}"
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        NetworkService.getService().sendLike(requestBody)
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

    // 좋아요 취소
    fun requestDislike(
        likeProfileId: String,
        networkFail: (String) -> Unit = {},
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {

        NetworkService.getService().sendDislike(likeProfileId)
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

    fun getLikedList(
        networkFail: (String) -> Unit,
        success: (ApiResponse<List<Someone>>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        NetworkService.getService().getLikedList()
            .enqueue(object : Callback<ApiResponse<List<Someone>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<Someone>>>,
                    response: Response<ApiResponse<List<Someone>>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        success(data)
                    } else {
                        networkFail(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<Someone>>>, t: Throwable) {
                    failure(t)
                }
            })
    }


}


