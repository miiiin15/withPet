package com.withpet.mobile.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.databinding.ActivityLoginBinding
import com.withpet.mobile.ui.activity.signup.SignupActivity

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupStartButton()
        setupRegisterButton()

        // 팝업 내부에 동적으로 콘텐츠를 추가하는 예제
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_select_gender_view, null)
        contentFrame.addView(customView)


        val cardView1: MaterialCardView = customView.findViewById(R.id.card_view_1)
        val cardView2: MaterialCardView = customView.findViewById(R.id.card_view_2)

        // 첫 번째 카드뷰에 클릭 리스너 추가
        cardView1.setOnClickListener {
            // 클릭 이벤트 처리
            Toast.makeText(this, "첫 번째 카드뷰가 클릭되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 두 번째 카드뷰에 클릭 리스너 추가
        cardView2.setOnClickListener {
            // 클릭 이벤트 처리
            Toast.makeText(this, "두 번째 카드뷰가 클릭되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupStartButton() {
        // TODO : 로그인 로직 처리
        binding.buttonStart.setOnClickListener {
        showPopup()
        }
    }

    private fun setupRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

}
