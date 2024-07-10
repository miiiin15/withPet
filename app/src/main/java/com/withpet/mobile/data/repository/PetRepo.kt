package com.withpet.mobile.data.repository

import com.google.gson.Gson
import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.utils.Logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object PetRepo {

    fun savePetInfo(
        petAddRequest: PetAddRequest,
        profileImagePath: String?,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit,
        finally: () -> Unit
    ) {
        val gson = Gson()
        val json = gson.toJson(petAddRequest)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val profileImagePart: MultipartBody.Part? = profileImagePath?.let {
            val file = File(it)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        }

        NetworkService.getService().requestSavePetInfo(requestBody, profileImagePart)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val data = response.body() ?: return
                            success(data)
                        } else {
                            networkFail(response.code().toString())
                        }
                    } finally {
                        finally()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                    try {
                        failure(t)
                    } finally {
                        finally()
                    }
                }
            })
    }
}

