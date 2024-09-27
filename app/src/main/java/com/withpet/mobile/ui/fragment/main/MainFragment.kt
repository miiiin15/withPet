package com.withpet.mobile.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.session.UserSession
import com.withpet.mobile.databinding.FragmentMainBinding
import com.withpet.mobile.ui.activity.main.SomeoneList
import com.withpet.mobile.ui.custom.SomeoneInfoBottomSheet
import com.withpet.mobile.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var userInfo: MemberInfo

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
                    userInfo = UserSession.getUserInfo()

                    UserSession.checkRegionInfo(
                        onAvailable = { regionInfo ->
                            showSomeoneInfoBottomSheet(someone)
                        },
                        onUnavailable = {
                            (activity as? BaseActivity)?.showRegionPopup()
                        }
                    )
                }
                // Like 버튼 클릭 리스너 설정
                someoneList.setOnLikeButtonClickListener(object :
                    SomeoneList.OnLikeRequestSuccess {
                    override fun onLikeRequestSuccess(currentLike: Boolean, success: Boolean) {
                        viewModel.handleLikeRequest(currentLike, success)
                    }
                }
                )
            }
        })

        viewModel.likeMessage.observe(viewLifecycleOwner, Observer { message ->
            (activity as? BaseActivity)?.showSnackBar(message)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            (activity as? BaseActivity)?.showAlert(errorMsg)
        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { throwable ->
            (activity as? BaseActivity)?.showAlert(throwable.message ?: "Unknown error")
        })

        // 벨 아이콘 클릭 리스너 설정
        binding.ivBellIcon.setOnClickListener {
            // TODO : 아이콘 클릭 시 동작할 코드 작성
            (activity as? BaseActivity)?.showAlert("알람 개발 중")
        }

        // onCreateView에서 데이터가 이미 로드되지 않았는지 확인
        if (!viewModel.isDataLoaded) {
            viewModel.fetchMatchedList()
        }

        return binding.root
    }

    private fun showSomeoneInfoBottomSheet(someone: Someone) {
        // BottomSheet를 표시하는 코드
        val bottomSheet = SomeoneInfoBottomSheet()

        // Someone 객체의 데이터를 BottomSheet에 설정
//        bottomSheet.setProfileImage(someone.profileImage) // profileImage는 Someone 객체에 있는 필드
        bottomSheet.setData(someone)


        bottomSheet.setOnGreetClickListener {
            // 인사하기 버튼 클릭 시 동작 설정
        }

        bottomSheet.setOnLikeClickListener {
            // 좋아요 버튼 클릭 시 동작 설정
        }

        bottomSheet.show(fragmentManager!!, bottomSheet.tag)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 객체 해제
        _binding = null
    }
}

