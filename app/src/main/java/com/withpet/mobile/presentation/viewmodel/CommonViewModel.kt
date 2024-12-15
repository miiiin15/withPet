package com.withpet.mobile.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.withpet.mobile.BaseViewModel
import com.withpet.mobile.domain.DataResouce
import com.withpet.mobile.domain.usecase.GetVersionUseCase
import com.withpet.mobile.presentation.state.VersionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommonViewModel(private val getVersionUseCase: GetVersionUseCase) : BaseViewModel() {
    private val _uiState = MutableStateFlow(VersionState.DEFAULT)
    val uiState = _uiState.asStateFlow()

    init {
        fetchVersion()
    }

    private fun fetchVersion() {
        viewModelScope.launch {
            getVersionUseCase()
                .onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect({ dataResource ->
                    when (dataResource) {
                        is DataResouce.Success ->
                            _uiState.update { uiState ->
                                uiState.copy(versionInfo = dataResource.data)
                            }

                        is DataResouce.Error -> _uiState.update { it.copy(error = dataResource.throwable) }
                        is DataResouce.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }

                })
        }
    }
}