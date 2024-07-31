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
import com.withpet.mobile.databinding.ActivitySplashBinding
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.activity.start.StartActivity
import com.withpet.mobile.utils.DataProvider
import com.withpet.mobile.utils.Logcat

class SplashActivity : BaseActivity() {
    companion object {
        private const val PERMISSION_REQUEST_ID = 11
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
            val permissionLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

            val permissionWriteStorage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                PackageManager.PERMISSION_GRANTED // Android 13 이상에서는 무시
            }

            val permissionReadStorage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                PackageManager.PERMISSION_GRANTED // Android 13 이상에서는 무시
            }

            if (permissionCamera == PackageManager.PERMISSION_DENIED ||
                permissionWriteStorage == PackageManager.PERMISSION_DENIED ||
                permissionReadStorage == PackageManager.PERMISSION_DENIED ||
                permissionLocation == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } else {
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
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
                initPermissions()
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
                    Toast.makeText(this, "자동 로그인 성공", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "실패: ${it.result.message}", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Logcat.e("에러: ${it.message}")
                Toast.makeText(this, "에러: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
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
