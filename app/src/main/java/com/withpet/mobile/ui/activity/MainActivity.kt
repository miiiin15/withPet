package com.withpet.mobile.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.withpet.mobile.R
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.databinding.ActivityMainBinding
import com.withpet.mobile.utils.Logcat

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
        setNavigationBar()

    }

    private fun setFragment() {

        // 카테고리 클릭 리스너 설정 (필요 시)
        binding.bottomNavigationBar.setOnClickListener {
            // TODO: 카테고리 클릭 시 동작 설정
        }
    }

    // 프래그먼트 로드 메서드
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setNavigationBar() {

        // 카테고리 클릭 리스너 설정 (필요 시)
        binding.bottomNavigationBar.setOnClickListener {
            // TODO: 카테고리 클릭 시 동작 설정
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