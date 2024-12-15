package com.withpet.mobile.di.impl

import com.withpet.mobile.di.AppModule
import com.withpet.mobile.di.CoroutineProviderModule
import com.withpet.mobile.di.DataModule
import com.withpet.mobile.di.DomainModule
import com.withpet.mobile.di.PresentationModule
import com.withpet.mobile.di.RemoteModule

class AppModuleImpl(
    private val coroutineProviderModule: CoroutineProviderModule,
    private val remoteModule: RemoteModule,
    private val dataModule: DataModule,
    private val domainModule: DomainModule,
    private val presentationModule: PresentationModule
):AppModule,
        CoroutineProviderModule by coroutineProviderModule,
        RemoteModule by remoteModule,
        DataModule by dataModule,
        DomainModule by domainModule,
        PresentationModule by presentationModule