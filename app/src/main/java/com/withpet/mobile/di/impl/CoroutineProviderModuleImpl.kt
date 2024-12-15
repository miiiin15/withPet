package com.withpet.mobile.di.impl

import com.withpet.mobile.di.CoroutineProviderModule
import com.withpet.mobile.provider.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers

// CoroutineProviderModule 구현체
class CoroutineProviderModuleImpl : CoroutineProviderModule{
    override val coroutineContextProvider: CoroutineContextProvider by lazy {
        // 실제 사용할 코루틴 컨텍스트 정의.
        object : CoroutineContextProvider{
            // Dispatchers.IO를 CoroutineContextProvider로 제공.
            override val io = Dispatchers.IO
        }
    }
}