package com.withpet.mobile.presentation.state

import com.withpet.mobile.data.api.response.VersionInfo

data class VersionState(
    val isLoading:Boolean,
    val versionInfo: VersionInfo,
    val error: Throwable?
){
    companion object{
        val DEFAULT = VersionState(
            isLoading = false,
            versionInfo = VersionInfo(0,""),
            error = null
        )
    }
}