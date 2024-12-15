package com.withpet.mobile.data.api.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @SerializedName("result")
    var result: ResultResponse, // 응답 상태


    var error: ApiError?, // 오류

    @SerializedName("payload")
    var payload: T // 응답 데이터
)

data class ResultResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String
)
