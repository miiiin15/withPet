package com.withpet.mobile.data.repository

import com.withpet.mobile.data.api.response.VersionInfo
import com.withpet.mobile.domain.DataResouce
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    fun getVersion(): Flow<DataResouce<VersionInfo>>
}