package com.withpet.mobile.di

import com.withpet.mobile.data.repository.MainRepo
import com.withpet.mobile.data.repository.SignInRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn
import javax.inject.Singleton

// 직접 호출하지 않고, Hilt가 필요한 곳에 자동으로 의존성을 주입해줍니다.

@Module
@InstallIn(SingletonComponent::class)
// 의존성 그래프를 구축할 때 사용
object RepositoryModule {
//    필요할 때 Hilt가 자동으로 이 함수에서 해당 객체를 가져와 주입합니다.
    @Provides
    @Singleton
    fun provideCommonRepo(): MainRepo {
        return MainRepo
    }

    @Provides
    @Singleton
    fun provideSignInRepo(): SignInRepo {
        return SignInRepo
    }
}
