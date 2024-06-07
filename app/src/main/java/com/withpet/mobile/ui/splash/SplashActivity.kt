package com.withpet.mobile.ui.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.MainActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.data.repository.SignInRepo
import com.withpet.mobile.ui.login.LoginActivity
import com.withpet.mobile.utils.DataProvider

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_ID = 11
        private var requestPermissionGranted = false
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)

        // 버전을 확인하고 이후 sharedPreferences를 검사하고 로직을 진행
        checkVersionAndNavigate()
    }

    private fun checkVersionAndNavigate() {
        CommonRepo.getVersion(
            success = {
                if (it.result.code == 200) {
                    // sharedPreferences를 검사하고 이후 로직을 진행
                    checkSharedPreferences()
                } else {
                    Toast.makeText(this, "${it.error?.message.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            networkFail = {
                Toast.makeText(this, "네트웍실패", Toast.LENGTH_SHORT).show()
            },
            failure = {
                Toast.makeText(this, "그냥실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun checkSharedPreferences() {
        // 로그인 정보가 존재하는지 확인하고, 있으면 자동 로그인 시도
        val loginId = sharedPreferences.getString("loginId", "")
        val password = sharedPreferences.getString("password", "")
        if (!loginId.isNullOrBlank() && !password.isNullOrBlank()) {
            signIn(loginId, password)
        } else {
            initPermissions()
        }
    }

    private fun initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCamera = checkSelfPermission(Manifest.permission.CAMERA)
            val permissionWriteStorage =
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val permissionLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

            if (permissionCamera == PackageManager.PERMISSION_DENIED ||
                permissionWriteStorage == PackageManager.PERMISSION_DENIED ||
                permissionLocation == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                requestPermissions(permissions, PERMISSION_REQUEST_ID)
            } else {
                requestPermissionGranted = true
                navigateToLogin()
            }
        } else {
            Log.e("권한", "PermissionGrant run with old way")
            requestPermissionGranted = true
            navigateToLogin()
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
            }
        }
    }

    private fun signIn(loginId: String, password: String) {
        SignInRepo.signIn(
            loginId = loginId,
            password = password,
            networkFail = {
                Toast.makeText(this, "네트워크 실패: $it", Toast.LENGTH_SHORT).show()
            },
            success = {
                if (it.result.code == 200) {
                    DataProvider.isLogin = true
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
