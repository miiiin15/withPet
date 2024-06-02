package com.withpet.mobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 초기화 로직 수행
        checkPermissionsAndVersion()

        // 초기화 완료 후 로그인 화면으로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000) // 2초 후 전환
    }

    private fun checkPermissionsAndVersion() {
        // 권한 및 버전 확인 로직
    }
}
