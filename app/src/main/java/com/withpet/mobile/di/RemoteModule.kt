package com.withpet.mobile.di

import com.withpet.mobile.data.api.ApiService
import com.withpet.mobile.data.datasource.CommonRemoteDataSource

interface RemoteModule {

    val apiService: ApiService

    val commonRemoteDataSource: CommonRemoteDataSource
}