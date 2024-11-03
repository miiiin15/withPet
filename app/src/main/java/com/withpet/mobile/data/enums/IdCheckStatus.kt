package com.withpet.mobile.data.enums

enum class IdCheckStatus {
    VALID,      // 사용 가능한 ID
    INVALID,    // 중복된 ID
    FORMAT_ERROR, // 이메일 형식 오류
    ERROR       // 기타 네트워크/서버 오류
}