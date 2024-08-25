package com.withpet.mobile.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.databinding.FragmentMainBinding
import com.withpet.mobile.ui.activity.main.SomeoneList
import com.withpet.mobile.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // SomeoneList 설정
        viewModel.matchedList.observe(viewLifecycleOwner, Observer { response ->
            response?.payload?.let { someones ->
                val someoneList: SomeoneList = binding.someoneList
                someoneList.setSomeones(someones) // 데이터 설정
                someoneList.setOnItemClickListener { someone ->
                    showSomeoneInfoBottomSheet(someone)
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            // BaseActivity를 통해 에러 메시지 표시
            (activity as? BaseActivity)?.showAlert(errorMsg)
        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { throwable ->
            // 실패 메시지 처리
            (activity as? BaseActivity)?.showAlert(throwable.message ?: "Unknown error")
        })

        // 벨 아이콘 클릭 리스너 설정
        binding.ivBellIcon.setOnClickListener {
            // 아이콘 클릭 시 동작할 코드 작성
            (activity as? BaseActivity)?.showAlert("알람 개발 중")
        }

        // 프래그먼트가 생성될 때 데이터를 로드
        viewModel.fetchMatchedList()

        return binding.root
    }

    private fun showSomeoneInfoBottomSheet(someone: Someone) {
        // BottomSheet를 표시하는 코드
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 객체 해제
        _binding = null
    }
}

