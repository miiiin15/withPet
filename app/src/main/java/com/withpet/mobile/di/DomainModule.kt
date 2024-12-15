package com.withpet.mobile.di

import com.withpet.mobile.domain.usecase.GetVersionUseCase

interface DomainModule {

    val getVersionUseCase: GetVersionUseCase
}