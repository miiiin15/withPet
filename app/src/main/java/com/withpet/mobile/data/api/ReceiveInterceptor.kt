package com.withpet.mobile.data.api

import okhttp3.Interceptor
import okhttp3.Response

// 응답 데이터를 로깅하고 쿠키 및 토큰을 저장.
class ReceiveInterceptor(private val tokenRepository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // 새로운 토큰 저장
        response.headers("X-ACCESS-TOKEN").firstOrNull()
            ?.let { tokenRepository.saveAccessToken(it) }
        response.headers("X-REFRESH-TOKEN").firstOrNull()
            ?.let { tokenRepository.saveRefreshToken(it) }

        return response
    }
}
