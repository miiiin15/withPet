package com.withpet.mobile.domain

sealed class DataResouce<out T> { // out 이 붙으면 읽기 전용 클래스
    // 모든 하위 클래스는 반드시 같은 파일에 정의되어야 함
    class Success<T>(val data:T) : DataResouce<T>()
    class Error(val throwable: Throwable) : DataResouce<Nothing>()
    //  로딩 중에 임시 데이터를 포함할 수 있도록 설계
    class Loading<T>(val dta: T? = null) : DataResouce<T>()

    companion object{
        // 팩토리 메서드를 통해 객체 생성 단순화.
        fun <T> success(data:T) = Success(data)
        fun error(throwable: Throwable) = Error(throwable)
        fun <T> loading(data: T? = null): Loading<T> = Loading(data)
    }
}