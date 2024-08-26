package com.withpet.mobile.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.withpet.mobile.R
import com.withpet.mobile.databinding.FragmentProfileBinding
import com.withpet.mobile.ui.activity.splash.SplashActivity
import com.withpet.mobile.utils.SharedPreferencesUtil

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // 로그아웃 버튼 클릭 리스너 설정
        binding.btnLogout.setOnClickListener {
            performLogout()
        }

        return binding.root
    }

    private fun performLogout() {
        // SharedPreferencesUtil을 사용하여 로그인 정보 지우기
        SharedPreferencesUtil.clearLoginInfo(requireContext())

        // 모든 화면 기록 지우고 SplashActivity로 이동
        val intent = Intent(requireContext(), SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티 종료
    }
}
