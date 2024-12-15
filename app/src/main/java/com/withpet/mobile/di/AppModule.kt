package com.withpet.mobile.di

interface AppModule :
    CoroutineProviderModule, // 코루틴 디스패처 관리
    RemoteModule, // 네트워크 관련 의존성 관리
    DataModule, // 리포지토리 구현체 관리
    DomainModule, // 비즈니스 로직 캡슐화
    PresentationModule // 뷰모델 의존성 관리