package com.withpet.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val commonRepo: MainRepo
) : BaseViewModel() {

    private val _likedList = MutableLiveData<ApiResponse<List<Someone>>>()
    val likedList: LiveData<ApiResponse<List<Someone>>> get() = _likedList

    var isDataLoaded = false

    var headerTitle = MutableLiveData<String>("")
    val disLikeMessage = MutableLiveData<String>()

    fun setHeaderTitle(newTitle: String) {
        headerTitle.value = newTitle
    }

    override fun fetchData(): Job = fetchLikedList()
    fun fetchLikedList(forceUpdate: Boolean = false): Job {
        return if (forceUpdate || !isDataLoaded) {
            launchDataLoad {
                val response = commonRepo.getLikedList(
                    networkFail = { errorMsg ->
                        _error.postValue(errorMsg)  // _error는 BaseViewModel에서 관리
                    },
                    success = { response ->
                        _likedList.postValue(response)  // 데이터 업데이트
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

    fun handleDeleteRequest(success: Boolean) {
        disLikeMessage.value = "좋아요에서 삭제" + if (success) "됐어요" else " 실패"
    }
}