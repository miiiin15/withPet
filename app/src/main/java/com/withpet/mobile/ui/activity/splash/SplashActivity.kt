package com.withpet.mobile.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.BuildConfig
import com.withpet.mobile.DiApplication.Companion.appModule
import com.withpet.mobile.data.repository.MainRepo
import com.withpet.mobile.data.session.UserSession
import com.withpet.mobile.databinding.ActivitySplashBinding
import com.withpet.mobile.presentation.viewmodel.CommonViewModel
import com.withpet.mobile.ui.activity.MainActivity
import com.withpet.mobile.ui.activity.start.StartActivity
import com.withpet.mobile.utils.Logcat
import com.withpet.mobile.utils.PermissionUtils
import com.withpet.mobile.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private val diViewModel : CommonViewModel by lazy { appModule.commonViewModel }

    companion object {
        const val PERMISSION_REQUEST_ID = 11
        private var requestPermissionGranted = false
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        binding.versionTextView.text = "버전 ${BuildConfig.VERSION_NAME}"
        setViewModel()

        observeUIState()

        // TODO : 서버 문제시 뚫기 위한 테스트 코드
        binding.appIconImageView.setOnClickListener {
            navigate("login")
        }

    }

    private fun setViewModel() {
        viewModel.error.observe(this) {
            showAlert(it)
        }
        viewModel.isLoading.observe(this) {
            if (it) {
                loadingDialog.show(supportFragmentManager, "")
            } else {
                loadingDialog.dismiss()
            }
        }
    }

   private fun observeUIState(){
       lifecycleScope.launchWhenCreated {
           diViewModel.uiState.collect{state->
               when{
                   state.error != null -> showAlert(state.error.message?:"메시지 없음")
                   state.versionInfo != null -> {
                       Toast.makeText(applicationContext, "버전 : ${state.versionInfo.version}", Toast.LENGTH_SHORT)
                           .show()
                       checkSharedPreferences()
                   }
               }
           }
       }
   }

    private fun checkSharedPreferences() {
        // 유저 정보 전역값 초기화
        UserSession.clearUserInfo()
        // 로그인 정보가 존재하는지 확인하고, 있으면 자동 로그인 시도
        val loginId = sharedPreferences.getString("loginId", "")
        val password = sharedPreferences.getString("password", "")
        if (!loginId.isNullOrBlank() && !password.isNullOrBlank()) {
            viewModel.logIn(loginId, password) {
                navigate("main")
            }
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

    private fun grantedPermissions() {
        requestPermissionGranted = true
        navigate("login")
    }

    private fun navigate(type: String) {
        var intent: Intent? = null
        when (type) {
            "login" -> {
                intent = Intent(this, StartActivity::class.java)
            }

            "main" -> {
                intent = Intent(this, MainActivity::class.java)
            }

            else -> {
                Logcat.e("올바르지 않은 값")
            }
        }
        if (intent != null) {
            startActivity(intent)
            finish()
        }
    }
}
