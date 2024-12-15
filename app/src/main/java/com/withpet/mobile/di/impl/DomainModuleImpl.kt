package com.withpet.mobile.di.impl

import com.withpet.mobile.DiApplication.Companion.appModule
import com.withpet.mobile.di.DomainModule
import com.withpet.mobile.domain.usecase.GetVersionUseCase
import com.withpet.mobile.domain.usecaseimpl.GetVersionUseCaseImpl

class DomainModuleImpl: DomainModule {

    override val getVersionUseCase: GetVersionUseCase by lazy {
        with(appModule){GetVersionUseCaseImpl(commonRepository)}
    }
}