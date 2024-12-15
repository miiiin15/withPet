package com.withpet.mobile.domain.usecaseimpl

import com.withpet.mobile.data.api.response.VersionInfo
import com.withpet.mobile.data.repository.CommonRepository
import com.withpet.mobile.domain.DataResouce
import com.withpet.mobile.domain.usecase.GetVersionUseCase
import kotlinx.coroutines.flow.Flow

class GetVersionUseCaseImpl(private val commonRepository: CommonRepository) : GetVersionUseCase{
    override fun invoke(): Flow<DataResouce<VersionInfo>> = commonRepository.getVersion()
}