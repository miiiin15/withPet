package com.withpet.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.withpet.mobile.R
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.databinding.ActivityMainBinding
import com.withpet.mobile.ui.activity.splash.SplashActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val navController = findNavController(R.id.nav_host_fragment_content_main)

        getMemberInfo()


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "안녕하세요?", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.logout.setOnClickListener { view ->
            val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, SplashActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMemberInfo() {
        CommonRepo.getMemberInfo(
            success = {
                if (it.result.code == 200) {
                    // sharedPreferences를 검사하고 이후 로직을 진행
                    Toast.makeText(
                        this,
                        "${it.payload.nickName} 님은 ${getRoleMessage(it.payload.role)}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(this, "${it.error?.message.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            networkFail = {
                Toast.makeText(this, "정보조회 실패 $it", Toast.LENGTH_SHORT).show()
            },
            failure = {
                Toast.makeText(this, "통신 실패 $it", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun getRoleMessage(role: String): String {
        return when (role) {
            "initial" -> "추가정보 입력이 필요합니다."
            "normal" -> "추가정보 입력을 완료하셨습니다."
            "admin" -> "관리자입니다."
            else -> "알 수 없는 역할입니다."
        }
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}