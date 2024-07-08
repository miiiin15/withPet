package com.withpet.mobile.ui.activity.login

import android.os.Bundle
import android.util.Log
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.api.NetworkService
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.databinding.ActivitySigninBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO : 응답 처리 추가하기
        binding.btnSignIn.setOnClickListener {
            val loginId = binding.etLoginId.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(loginId, password,
                { message -> Log.d("로", "Network Fail: $message") },
                { response -> Log.d("로", "Success: ${response.payload}") },
                { throwable -> Log.d("로긴", "Failure: ${throwable.message}") }
            )
        }
    }

    private fun signIn(
        loginId: String,
        password: String,
        networkFail: (String) -> Unit,
        success: (ApiResponse<Any>) -> Unit,
        failure: (Throwable) -> Unit
    ) {

        Log.d("LoginActivity", "$loginId / $password")
        val jsonBody = "{\"loginId\": \"$loginId\", \"password\": \"$password\"}"
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
