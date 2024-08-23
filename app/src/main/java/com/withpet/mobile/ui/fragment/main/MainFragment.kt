package com.withpet.mobile.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.databinding.FragmentMainBinding
import com.withpet.mobile.ui.activity.splash.SplashActivity
import com.withpet.mobile.utils.Logcat
import com.withpet.mobile.utils.SharedPreferencesUtil
import com.withpet.mobile.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null // onDestroyView 용 nullable
    private val binding get() = _binding!! // non-nullable로 사용하여 안전하게 뷰 요소에 접근할 수 있게 합니다.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // LiveData를 관찰하여 UI 업데이트
        viewModel.matchedList.observe(viewLifecycleOwner, Observer { response ->
            // 데이터를 UI에 표시
            Logcat.d("응답 값은? : ${response.payload[0]
            }")
//            binding.recyclerView.adapter?.submitList(response.data)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            // 에러 메시지 UI에 표시
            (activity as? BaseActivity)?.showAlert(errorMsg)

        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { throwable ->
            // 실패 메시지 처리 (예: 토스트)
            (activity as? BaseActivity)?.showAlert(throwable.message.toString())
        })

        // 프래그먼트가 생성될 때 데이터를 로드
        viewModel.fetchMatchedList()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 객체 해제
        _binding = null
        // TODO :  리스너나 Adapter 등을 사용하고 있다면, 해제하거나 정리하는 소스 추가하기
    }
}
