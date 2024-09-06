package com.withpet.mobile.data.session

import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.api.response.RegionInfo

class UserSession {

    companion object {
        // 유저 정보를 저장할 변수
        private var userInfo: MemberInfo? = null

        // 유저 정보를 설정하는 메서드
        fun setUserInfo(info: MemberInfo) {
            userInfo = info
        }

        // 유저 정보를 가져오는 메서드
        fun getUserInfo(): MemberInfo {
            return userInfo ?: throw IllegalStateException("유저정보가 없습니다.")
        }

        // 유저 정보 초기화 메서드
        fun clearUserInfo() {
            userInfo = null
        }

        // region이 null인지 확인하고 콜백을 호출하는 메서드
        fun checkRegionInfo(
            onAvailable: ((RegionInfo) -> Unit)? = null,
            onUnavailable: (() -> Unit)? = null
        ) {
            val region = userInfo?.region
            if (region != null) {
                // region 정보가 있을 때 호출되는 콜백에 region을 전달
                onAvailable?.invoke(region)
            } else {
                // region 정보가 없을 때 호출되는 콜백
                onUnavailable?.invoke()
            }
        }
    }
}
