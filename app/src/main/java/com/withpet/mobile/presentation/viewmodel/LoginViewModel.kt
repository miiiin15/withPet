package com.withpet.mobile.viewmodel

import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.utils.DataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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

