package com.withpet.mobile.ui.activity.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.BuildConfig
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.data.session.UserSession
import com.withpet.mobile.databinding.ActivitySplashBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.activity.start.StartActivity
import com.withpet.mobile.utils.PermissionUtils

class SplashActivity : BaseActivity() {
    companion object {
        const val PERMISSION_REQUEST_ID = 11
        private var requestPermissionGranted = false
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        binding.tvAppVersion.text = "버전 ${BuildConfig.VERSION_NAME}"

        // 버전을 확인하고 이후 sharedPreferences를 검사하고 로직을 진행
        checkVersionAndNavigate()

        // TODO : 서버 문제시 뚫기 위한 테스트 코드
        binding.imgAppIconCenter.setOnClickListener {
            navigateToLogin()
        }

    }

    private fun checkVersionAndNavigate() {
        loadingDialog.show(supportFragmentManager, "")
        try {
            CommonRepo.getVersion(
                success = {
                    if (it.result.code == 200) {
                        // sharedPreferences를 검사하고 이후 로직을 진행
                        Toast.makeText(this, "버전 : ${it.payload.version}", Toast.LENGTH_SHORT)
                            .show()
                        checkSharedPreferences()
                    } else {
                        showAlert("${it.error?.message.toString()}")
                    }
                },
                networkFail = {
                    showAlert(it)
                },
                failure = {
                    showAlert(it.message ?: "fail")
                }
            )
        } catch (e: Exception) {
        } finally {
            loadingDialog.dismiss()
        }
    }

    private fun checkSharedPreferences() {
        // 유저 정보 전역값 초기화
        UserSession.clearUserInfo()
        // 로그인 정보가 존재하는지 확인하고, 있으면 자동 로그인 시도
        val loginId = sharedPreferences.getString("loginId", "")
        val password = sharedPreferences.getString("password", "")
        if (!loginId.isNullOrBlank() && !password.isNullOrBlank()) {
            logIn(loginId, password)
        } else {
            PermissionUtils.initPermissions(this, this) {
                grantedPermissions()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ID) {
            requestPermissionGranted =
                grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (requestPermissionGranted) {
                // 권한이 획득되었으면 sharedPreferences를 검사하고 이후 로직을 진행
                checkSharedPreferences()
            } else {
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                PermissionUtils.initPermissions(this, this) {
                    grantedPermissions()
                }
            }
        }
    }

    private fun logIn(loginId: String, password: String) {
        loadingDialog.show(supportFragmentManager, "")
        try {
            SignInRepo.logIn(
                loginId = loginId,
                password = password,
                success = {
                    if (it.result.code == 200) {
                        navigateToMainActivity()
                    } else {
                        showAlert("로그인 실패: ${it.result.message}")
                    }
                },
                networkFail = {
                    showAlert("로그인 네트워크 실패: $it")
                },
                failure = {
                    showAlert("로그인 에러: ${it.message}")
                }
            )
        } catch (e: Exception) {
        } finally {
            loadingDialog.dismiss()
        }
    }

    private fun grantedPermissions() {
        requestPermissionGranted = true
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
