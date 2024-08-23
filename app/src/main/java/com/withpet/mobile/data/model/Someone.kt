package com.withpet.mobile.data.model

data class Someone(
    val memberId: Int,
    val nickName: String,
    val age: Int,
    val sexType: String,
    val regionName: String,
    val profileImage: String?,
    val introduction: String?,
    val petSize: String,
    val petSex: String,
    val petAge: Int,
    val like: Boolean
)
