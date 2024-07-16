package com.withpet.mobile.data.model

data class Someone(
    val username: String,
    val address: String,
    val gender: String,
    val profileImage: String? // 프로필 이미지는 nullable로 설정
)
