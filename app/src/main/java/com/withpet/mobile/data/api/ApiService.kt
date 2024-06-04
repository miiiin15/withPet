package com.withpet.mobile.data.api

import com.withpet.mobile.data.api.response.ApiResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    /**
     * 공통
     */

    // 버전 조회
    @GET("api/v1/version")
    fun getVersion(): Call<ApiResponse<Any>>

    // 로그인
    @POST("api/auth/sign-in")
    fun requestSignIn(@Body params: RequestBody): Call<ApiResponse<String>>

    // 회원가입
    @POST("api/auth/sign-up")
    fun requestSignUp(@Body params: RequestBody): Call<ApiResponse<Any>>

    // 내정보 조회
//    @GET("api/member")
//    fun getUserInfo(): Call<ApiResponse<UserInfo>>



    // 위치 저장
    @POST("api/location")
    fun saveLocationList(@Body params: RequestBody): Call<ApiResponse<String>>

    // 추천 코드 기반 위치 조회
    @GET("api/location/{recommenderCode}")
    fun getLocationList(@Path("recommenderCode") recommenderCode: String): Call<ApiResponse<MutableList<Any>>>

    //약관 조회
//    @POST("api/policy")
//    fun requestPolicy(@Body params: RequestBody): Call<ApiResponse<List<Policy>>>

    // 약관 상세 조회
//    @GET("api/policy/auth")
//    fun getPolicyDetail(@Query("cd") cd: String): Call<ResponseBody>

//    @GET("api/lookup/auth/list")
//    fun getAuthType(): Call<ApiResponse<List<AuthType>>>


}

