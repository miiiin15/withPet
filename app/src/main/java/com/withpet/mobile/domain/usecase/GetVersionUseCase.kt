package com.withpet.mobile.domain.usecase

import com.withpet.mobile.data.api.response.VersionInfo
import com.withpet.mobile.domain.DataResouce
import kotlinx.coroutines.flow.Flow

interface GetVersionUseCase {
    operator fun invoke(): Flow<DataResouce<VersionInfo>>
}