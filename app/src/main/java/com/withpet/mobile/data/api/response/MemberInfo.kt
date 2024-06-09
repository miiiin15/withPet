package com.withpet.mobile.data.api.response

data class MemberInfo(
    val id: Int,
    val loginId: String,
    val nickName: String,
    val sexType: String,
    val age: Int,
    val role: String
)
