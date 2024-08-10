package com.withpet.mobile.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.enums.Category
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.databinding.ActivityMainBinding
import com.withpet.mobile.ui.custom.BottomNavigationBar

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    val navOptions = NavOptions.Builder()
        .setPopUpTo(R.id.mainFragment, true) // mainFragment를 제외한 모든 백스택 제거
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        getMemberInfo()
        setNavigationBar()

        // OnBackPressedDispatcher를 사용하여 뒤로가기 버튼을 처리
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentCategory = binding.bottomNavigationBar.getCategory()

                if (currentCategory != Category.MAIN) {
                    // 현재 카테고리가 MAIN이 아니면 MAIN 프래그먼트로 이동
                    binding.bottomNavigationBar.setCategory(Category.MAIN)
                    findNavController(R.id.nav_host_fragment_content_main).navigate(
                        R.id.action_global_mainFragment,
                        null,
                        navOptions
                    )
                } else {
                    // 현재 카테고리가 MAIN이면 showExitAlert() 실행
                    showExitAlert()
                }
            }
        })

    }


    private fun setNavigationBar() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.bottomNavigationBar.setOnCategorySelectedListener(object :
            BottomNavigationBar.OnCategorySelectedListener {
            override fun onCategorySelected(category: Category) {

                when (category) {
                    Category.MAIN -> navController.navigate(R.id.action_global_mainFragment)
                    Category.CHAT -> navController.navigate(R.id.action_global_chatFragment)
                    Category.MATCH -> navController.navigate(R.id.action_global_matchFragment)
                    Category.WALK -> navController.navigate(R.id.action_global_walkFragment)
                    Category.PROFILE -> navController.navigate(R.id.action_global_profileFragment)
                }
            }
        })
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