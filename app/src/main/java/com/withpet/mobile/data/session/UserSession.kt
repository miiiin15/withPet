package com.withpet.mobile.data.session

import com.withpet.mobile.data.api.response.MemberInfo

object UserSession {
    private var memberInfo: MemberInfo? = null

    fun setMemberInfo(info: MemberInfo) {
        memberInfo = info
    }

    fun getMemberInfo(): MemberInfo? {
        return memberInfo
    }

    fun clear() {
        memberInfo = null
    }
}