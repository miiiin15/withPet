package com.withpet.mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    protected val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<Throwable> get() = _failure

    open fun fetchData(): Job = Job()

    protected fun launchDataLoad(block: suspend () -> Unit): Job {
        _isLoading.postValue(true)
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
