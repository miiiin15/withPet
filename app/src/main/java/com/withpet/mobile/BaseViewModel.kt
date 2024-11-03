package com.withpet.mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    protected val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<Throwable> get() = _failure

    open fun fetchData(): Job = Job()

    // TODO : NetworkOnMainThreadException 스레드 정리 및 observe 후속 액션 확인하기
    protected fun launchDataLoad(block: suspend () -> Unit): Job {
        _isLoading.value = true
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
