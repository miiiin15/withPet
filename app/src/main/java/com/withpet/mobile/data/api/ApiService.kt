package com.withpet.mobile.data.api

import com.withpet.mobile.data.api.response.ApiResponse
import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.data.api.response.VersionPayload
import com.withpet.mobile.data.model.Someone
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    /**
     * 공통
     */

    // 버전 조회
    @GET("api/v1/version")
    fun getVersion(): Call<ApiResponse<VersionPayload>>

    // 내정보 조회
    @GET("api/v1/member")
    fun getMemberInfo(): Call<ApiResponse<MemberInfo>>

    // 로그인
    @POST("api/v1/auth/sign-in")
    fun requestSignIn(@Body params: RequestBody): Call<ApiResponse<Any>>

    // 회원가입
    @POST("api/v1/auth/sign-up")
    fun requestSignUp(@Body params: RequestBody): Call<ApiResponse<Any>>

    // 이메일 중복 검사( 회원 여부 조회 )
    @GET("api/v1/auth/check/duplicate")
    fun getCheckDuplicate(@Query("loginId") loginId: String): Call<ApiResponse<Any>>

    // 반려견 정보입력
    @Multipart
    @POST("api/v1/pet-info/save")
    fun requestSavePetInfo(
        @Part("petAddRequest") petAddRequest: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): Call<ApiResponse<Any>>

    // 내 지역 저장
    @POST("api/v1/region")
    fun saveLocationList(@Body params: RequestBody): Call<ApiResponse<Any>>

    // TODO : 반환 자료형 정의 할 것
    // 매칭 정보 조회
    @GET("api/v1/match")
    fun getMatchingList(): Call<ApiResponse<Any>>

    // 좋아요
    @POST("api/v1/like")
    fun requestLike(@Body params: RequestBody): Call<ApiResponse<Any>>

    // 좋아요 취소
    @POST("api/v1/like")
    fun requestDislike(@Query("likeProfileId") likeProfileId: String): Call<ApiResponse<Any>>

    // 추천 코드 기반 위치 조회
    @GET("api/location/{recommenderCode}")
    fun getLocationList(@Path("recommenderCode") recommenderCode: String): Call<ApiResponse<MutableList<Any>>>

    //약관 조회
//    @POST("api/policy")
//    fun requestPolicy(@Body params: RequestBody): Call<ApiResponse<List<Policy>>>


//    @GET("api/lookup/auth/list")
//    fun getAuthType(): Call<ApiResponse<List<AuthType>>>


}

