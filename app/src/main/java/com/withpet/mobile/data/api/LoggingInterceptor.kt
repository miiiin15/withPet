package com.withpet.mobile.data.api

import okhttp3.logging.HttpLoggingInterceptor

// HTTP 요청 및 응답 로그를 출력.
object LoggingInterceptor {
    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}