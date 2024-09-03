package com.withpet.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.CommonRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _matchedList = MutableLiveData<ApiResponse<List<Someone>>>()
    val matchedList: LiveData<ApiResponse<List<Someone>>> get() = _matchedList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<Throwable> get() = _failure

    var isDataLoaded = false

    // 데이터를 강제 갱신하거나 필요할 때만 API 호출
    fun fetchMatchedList(forceUpdate: Boolean = false) {
        if (forceUpdate || !isDataLoaded) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    CommonRepo.getMatchedList(
                        networkFail = { errorMsg ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _error.value = errorMsg
                            }
                        },
                        success = { response ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _matchedList.value = response
                                isDataLoaded = true // 데이터가 로드되었음을 표시
                            }
                        },
                        failure = { throwable ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _failure.value = throwable
                            }
                        }
                    )
                } catch (e: Exception) {
                    // 예외 처리
                    viewModelScope.launch(Dispatchers.Main) {
                        _error.value = e.message
                    }
                }
            }
        }
    }

    // 유저 상호작용에 의해 리스트가 변동될 때 데이터를 업데이트하는 메서드
    fun updateMatchedList(updatedList: ApiResponse<List<Someone>>) {
        _matchedList.value = updatedList
    }
}

