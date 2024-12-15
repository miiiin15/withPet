package com.withpet.mobile.di.impl

import com.withpet.mobile.DiApplication.Companion.appModule
import com.withpet.mobile.data.api.ApiService
import com.withpet.mobile.data.datasource.CommonRemoteDataSource
import com.withpet.mobile.data.datasourceimpl.CommonRemoteDataSourceImpl
import com.withpet.mobile.di.RemoteModule

class RemoteModuleImpl : RemoteModule {

    override val commonRemoteDataSource: CommonRemoteDataSource by lazy {
        with(appModule){CommonRemoteDataSourceImpl(apiService)}
    }

    override val apiService: ApiService
        get() = // TODO createApiService 확인하기
}