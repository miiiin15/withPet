package com.withpet.mobile.data.api.response

data class ApiResponse<T>(
    // 응답 상태
    var result: Result,

    // 오류
    var error: ApiError?,

    // 응답 데이터
    var payload: T
)

// 응답 상태를 나타내는 Result 클래스
data class Result(
    val code: Int,
    val message: String
)
