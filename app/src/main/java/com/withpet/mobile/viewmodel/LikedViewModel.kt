package com.withpet.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.utils.Logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikedViewModel : ViewModel() {

    private val _likedList = MutableLiveData<ApiResponse<List<Someone>>>()
    val likedList: LiveData<ApiResponse<List<Someone>>> get() = _likedList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<Throwable> get() = _failure

    var isDataLoaded = false


    var headerTitle = MutableLiveData<String>("")
    val disLikeMessage = MutableLiveData<String>()

    fun setHeaderTitle(newTitle: String) {
        headerTitle.value = newTitle
    }


    fun fetchLikedList(forceUpdate: Boolean = false) {
        if (forceUpdate || !isDataLoaded) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    CommonRepo.getLikedList(
                        networkFail = { errorMsg ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _error.value = errorMsg
                            }
                        },
                        success = { response ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                Logcat.e("${response.payload}")
                                _likedList.value = response
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
                    viewModelScope.launch(Dispatchers.Main) {
                        _error.value = e.message
                    }
                }
            }
        }
    }

    fun handleDeleteRequest(success: Boolean) {
        disLikeMessage.value = "좋아요에서 삭제" + if (success) "됐어요" else " 실패"
    }
}