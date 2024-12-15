package com.withpet.mobile.di.impl

import com.withpet.mobile.DiApplication.Companion.appModule
import com.withpet.mobile.data.repository.CommonRepository
import com.withpet.mobile.data.repositoryimpl.CommonRepositoryImpl
import com.withpet.mobile.di.DataModule

class DataModuleImpl : DataModule {
    override val commonRepository: CommonRepository by lazy {
        with(appModule) { CommonRepositoryImpl(commonRemoteDataSource) }
    }
}