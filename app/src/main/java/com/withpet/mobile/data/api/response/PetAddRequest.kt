package com.withpet.mobile.data.api.response

data class PetAddRequest(
    val size: String,
    val sex: String,
    val age: Int,
    val introduction: String,
    val memberId: Int
)