package com.withpet.mobile.data.datasourceimpl

import com.withpet.mobile.data.api.ApiService
import com.withpet.mobile.data.api.response.VersionInfo
import com.withpet.mobile.data.datasource.CommonRemoteDataSource

class CommonRemoteDataSourceImpl(private val apiService: ApiService)
    : CommonRemoteDataSource{
    override suspend fun getVersion(): VersionInfo {
        return apiService.getVersion().payload
    }
}