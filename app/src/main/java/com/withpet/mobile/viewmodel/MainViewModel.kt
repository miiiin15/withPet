package com.withpet.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.data.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _matchedList = MutableLiveData<ApiResponse<List<Someone>>>()
    val matchedList: LiveData<ApiResponse<List<Someone>>> get() = _matchedList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<Throwable> get() = _failure

    val isLoading = MutableLiveData<Boolean>(false)
    val likeMessage = MutableLiveData<String>()
    val address = MutableLiveData<String>()

    var isDataLoaded = false

    // 데이터를 강제 갱신하거나 필요할 때만 API 호출
    fun fetchMatchedList(forceUpdate: Boolean = false) {
        if (forceUpdate || !isDataLoaded) {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    CommonRepo.getMatchedList(
                        networkFail = { errorMsg ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _error.value = errorMsg
                                isLoading.value = false
                            }
                        },
                        success = { response ->
                            // Main thread에서 UI 업데이트
                            viewModelScope.launch(Dispatchers.Main) {
                                _matchedList.value = response
                                isDataLoaded = true // 데이터가 로드되었음을 표시
                                isLoading.value = false
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
                        isLoading.value = false
                    }
                }
            }
        }
    }

    // 유저 상호작용에 의해 리스트가 변동될 때 데이터를 업데이트하는 메서드
    fun updateMatchedList(updatedList: ApiResponse<List<Someone>>) {
        _matchedList.value = updatedList
    }

    fun handleLikeRequest(currentLike: Boolean, success: Boolean) {
        val message = if (currentLike) {
            "좋아요에서 삭제" + if (success) "됐어요" else " 실패"
        } else {
            "좋아요에 저장" + if (success) "했어요!" else " 실패"
        }
        likeMessage.value = message
    }

    fun fetchAdressText(onUnavailable: ()-> Unit){
        UserSession.checkRegionInfo(
            onAvailable = { regionInfo ->
                address.value = regionInfo.regionName
            },
            onUnavailable = {
                onUnavailable.invoke()
            }
        )
    }
}

