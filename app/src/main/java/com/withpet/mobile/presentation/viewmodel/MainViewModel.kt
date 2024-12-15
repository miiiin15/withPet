package com.withpet.mobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.MainRepo
import com.withpet.mobile.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val commonRepo: MainRepo
) : BaseViewModel() {

    private val _matchedList = MutableLiveData<ApiResponse<List<Someone>>>()
    val matchedList: LiveData<ApiResponse<List<Someone>>> get() = _matchedList

    val likeMessage = MutableLiveData<String>()
    val address = MutableLiveData<String>()

    var isDataLoaded = false

    override fun fetchData(): Job = fetchMatchedList()


    // 데이터를 강제 갱신하거나 필요할 때만 API 호출
    fun fetchMatchedList(forceUpdate: Boolean = false): Job {
        return if (forceUpdate || !isDataLoaded) {
            launchDataLoad {
                val response = commonRepo.getMatchedList(
                    networkFail = { errorMsg ->
                        _error.postValue(errorMsg)  // _error는 BaseViewModel에서 관리
                    },
                    success = { response ->
                        _matchedList.postValue(response)  // 데이터 업데이트
                        isDataLoaded = true  // 데이터가 로드되었음을 표시
                    },
                    failure = { throwable ->
                        _failure.postValue(throwable)
                    }
                )
            }
        } else {
            Job()  // 이미 로드된 경우, 빈 Job 반환
        }
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

