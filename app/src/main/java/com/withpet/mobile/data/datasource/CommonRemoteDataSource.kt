package com.withpet.mobile.data.datasource

import com.withpet.mobile.data.api.response.VersionInfo

interface CommonRemoteDataSource {
    suspend fun getVersion() : VersionInfo
}