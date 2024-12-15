package com.withpet.mobile.di

import com.withpet.mobile.provider.CoroutineContextProvider

// 코루틴 컨텍스트를 제공하는 역할을 명시.
interface CoroutineProviderModule {
    val coroutineContextProvider: CoroutineContextProvider
}