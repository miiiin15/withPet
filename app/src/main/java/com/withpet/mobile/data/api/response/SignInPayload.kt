package com.withpet.mobile.data.api.response

data class SignInPayload(
    val accessToken: String,
    val refreshToken: String,
    val expireRefreshTokenTime: String
)