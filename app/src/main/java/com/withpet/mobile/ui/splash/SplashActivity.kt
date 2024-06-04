package com.withpet.mobile.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 초기화 로직 수행
        checkPermissionsAndVersion()

        // 초기화 완료 후 로그인 화면으로 전환
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }, 2000) // 2초 후 전환
    }

    private fun checkPermissionsAndVersion() {
        // 권한 및 버전 확인 로직
        CommonRepo.getVersion(
            success = {
                if (it.result.code == 200) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this, "최신버전은 : ${it.payload.version} 입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "${it.error?.message.toString()}", Toast.LENGTH_SHORT).show()
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
}
