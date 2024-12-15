package com.withpet.mobile

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.withpet.mobile.di.AppModule
import com.withpet.mobile.di.impl.AppModuleImpl
import com.withpet.mobile.di.impl.CoroutineProviderModuleImpl
import com.withpet.mobile.di.impl.DataModuleImpl
import com.withpet.mobile.di.impl.DomainModuleImpl
import com.withpet.mobile.di.impl.PresentationModuleImpl
import com.withpet.mobile.di.impl.RemoteModuleImpl
import com.withpet.mobile.utils.DataProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initAppModule()
        // 앱 초기화 작업 수행
        DataProvider.init(this)

        // 라이트 모드 강제
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }

    private fun initAppModule(){
        val coroutineProviderModule = CoroutineProviderModuleImpl() // 코루틴 디스패처를 관리
        val remoteModule = RemoteModuleImpl() // 네트워크 관련 의존성을 관리
        val dataModule = DataModuleImpl() // 리포지토리 구현체를 관리
        val domainModule = DomainModuleImpl() // 비즈니스 로직을 캡슐화
        val presentationModule = PresentationModuleImpl() // 뷰모델 의존성을 관리

        appModule = AppModuleImpl(
            coroutineProviderModule = coroutineProviderModule,
            remoteModule = remoteModule,
            dataModule = dataModule,
            domainModule = domainModule,
            presentationModule = presentationModule,
        ) // 전체 의존성 주입 구조의 중앙 허브 역할.
    }


    companion object{
        lateinit var appModule: AppModule
    }

}
