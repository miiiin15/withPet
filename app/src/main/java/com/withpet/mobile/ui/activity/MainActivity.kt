package com.withpet.mobile.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.enums.Category
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.databinding.ActivityMainBinding
import com.withpet.mobile.ui.custom.BottomNavigationBar
import com.withpet.mobile.ui.custom.CustomButton

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

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
                    checkRole(it.payload.role)
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



    fun checkRole(role: String) {
        when (role) {
            "initial" -> showPositionPopup()
            "normal" -> null
            "admin" -> null
            else -> null
        }
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}