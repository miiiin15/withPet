package com.withpet.mobile.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.withpet.mobile.utils.Constants.SERVER_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30

fun createApiService(context: Context): ApiService{
    val tokenRepository = TokenRepository(context)

    val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(AddInterceptor(tokenRepository)) // 요청 인터셉터
        addInterceptor(ReceiveInterceptor(tokenRepository)) // 응답 인터셉터
        addInterceptor(LoggingInterceptor.create()) // 로깅 인터셉터
        readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        // TODO : 공부 할 것
//        addNetworkInterceptor()
    }.build()

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    return Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)
}