package com.withpet.mobile.data.session

import com.withpet.mobile.data.api.response.MemberInfo

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
    }
}
