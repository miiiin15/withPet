package com.withpet.mobile.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
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

//        // 팝업 내부에 동적으로 콘텐츠를 추가하는 예제
//        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
//        val customView = LayoutInflater.from(this).inflate(R.layout.custom_view, null)
//        contentFrame.addView(customView)
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
