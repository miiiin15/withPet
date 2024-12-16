package com.withpet.mobile.data.api

import okhttp3.Interceptor
import okhttp3.Response

// 요청 헤더에 토큰 및 쿠키 추가.
class AddInterceptor(private val tokenRepository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        // 최신 토큰 가져오기
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()

        // 토큰이 존재하면 헤더에 추가
        accessToken?.let { builder.addHeader("X-ACCESS-TOKEN", it) }
        refreshToken?.let { builder.addHeader("X-REFRESH-TOKEN", it) }

        // 추가적인 공통 헤더
        builder.addHeader("User-Agent", "Android-App")

        return chain.proceed(builder.build())
    }
}