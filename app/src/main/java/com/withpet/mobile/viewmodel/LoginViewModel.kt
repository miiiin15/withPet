package com.withpet.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.data.session.UserSession
import com.withpet.mobile.utils.DataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRepo: SignInRepo
) : BaseViewModel() {
    fun logIn(loginId: String, password: String, onSuccess: () -> Unit) {
        launchDataLoad {
            try {
                val result = signInRepo.logIn(loginId, password)
                if (result.payload == true) {
                    DataProvider.isLogin = true
                    onSuccess.invoke()
                } else {
                    throw Exception(result.result.message)
                }
            } catch (e: Exception) {
                throw Exception(e.message)
            }
        }
    }
}

