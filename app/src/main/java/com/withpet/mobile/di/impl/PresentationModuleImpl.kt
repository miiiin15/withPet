package com.withpet.mobile.di.impl

import com.withpet.mobile.DiApplication.Companion.appModule
import com.withpet.mobile.di.PresentationModule
import com.withpet.mobile.presentation.viewmodel.CommonViewModel

class PresentationModuleImpl : PresentationModule {
    override val commonViewModel: CommonViewModel by lazy {
        with(appModule) { CommonViewModel(getVersionUseCase) }
    }
}