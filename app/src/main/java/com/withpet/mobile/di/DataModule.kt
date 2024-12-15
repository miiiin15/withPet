package com.withpet.mobile.di

import com.withpet.mobile.data.repository.CommonRepository

interface DataModule {

    val commonRepository: CommonRepository
}