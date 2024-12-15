package com.withpet.mobile.data.repositoryimpl

import com.withpet.mobile.data.api.response.VersionInfo
import com.withpet.mobile.data.datasource.CommonRemoteDataSource
import com.withpet.mobile.data.repository.CommonRepository
import com.withpet.mobile.domain.DataResouce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommonRepositoryImpl(private val commonRemoteDataSource: CommonRemoteDataSource) :
    CommonRepository {
    override fun getVersion(): Flow<DataResouce<VersionInfo>> = flow {
        emit(DataResouce.loading())
        try {
            val version = commonRemoteDataSource.getVersion()
            emit(DataResouce.success(version))
        } catch (e: Exception) {
            emit(DataResouce.error(e))
        }
    }
}