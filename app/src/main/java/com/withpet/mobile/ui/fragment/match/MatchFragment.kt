package com.withpet.mobile.ui.fragment.match

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.api.response.MemberInfo
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.session.UserSession
import com.withpet.mobile.databinding.FragmentMatchBinding
import com.withpet.mobile.ui.activity.Location.LocationSearchActivity
import com.withpet.mobile.ui.activity.liked.LikedListActivity
import com.withpet.mobile.ui.activity.main.SomeoneList
import com.withpet.mobile.ui.custom.MatchedList
import com.withpet.mobile.ui.custom.SomeoneInfoBottomSheet
import com.withpet.mobile.utils.Logcat
import com.withpet.mobile.viewmodel.MainViewModel

class MatchFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMatchBinding? = null

    private val binding get() = _binding!!
    private lateinit var userInfo: MemberInfo

    private val likedListLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val isItemDeleted = result.data?.getBooleanExtra("isItemDeleted", false) ?: false
                if (isItemDeleted) {
                    viewModel.fetchMatchedList(true)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupViewModel()

        setupUI()

        return binding.root
    }

    private fun setupViewModel() {

        viewModel.fetchAdressText() {
            (activity as? BaseActivity)?.showRegionPopup()
        }

        viewModel.matchedList.observe(viewLifecycleOwner) { response ->
            response?.payload?.let { someones ->
                val someoneList: MatchedList = binding.matchedList
                someoneList.setSomeones(someones) // 데이터 설정
                someoneList.setOnItemClickListener { someone ->

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
                    MatchedList.OnLikeRequestSuccess {
                    override fun onLikeRequestSuccess(currentLike: Boolean, success: Boolean) {
                        viewModel.handleLikeRequest(currentLike, success)
                    }
                }
                )
            }

        }

        viewModel.likeMessage.observe(viewLifecycleOwner, Observer { message ->
            (activity as? BaseActivity)?.showSnackBar(message)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            (activity as? BaseActivity)?.showAlert(errorMsg)
        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { throwable ->
            (activity as? BaseActivity)?.showAlert(throwable.message ?: "Unknown error")
        })

        viewModel.address.observe(viewLifecycleOwner, Observer { adress ->
            adress.let { it -> binding.tvLocation.text = it }
        })

        // onCreateView에서 데이터가 이미 로드되지 않았는지 확인
        if (!viewModel.isDataLoaded) {
            viewModel.fetchMatchedList()
        }
    }

    private fun setupUI() {

        binding.ivBellIcon.setOnClickListener {
            // TODO : 아이콘 클릭 시 동작할 코드 작성
            (activity as? BaseActivity)?.showAlert("알람 개발 중")
        }

        binding.ivHearthIcon.setOnClickListener {
            val intent = Intent(requireContext(), LikedListActivity::class.java)
            likedListLauncher.launch(intent)
        }

        binding.tvLocation.setOnClickListener {
            // TODO : 위치 저장시 서버에 데이터가 업데이트가 아닌 누적되는 오류 발견하여 주석
            (activity as BaseActivity).showAlert("위치 설정 이동 주석 처리")
//            val intent = Intent(requireContext(), LocationSearchActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun showSomeoneInfoBottomSheet(someone: Someone) {
        // BottomSheet를 표시하는 코드
        val bottomSheet = SomeoneInfoBottomSheet()

        // Someone 객체의 데이터를 BottomSheet에 설정
        bottomSheet.setData(someone)


        bottomSheet.setOnGreetClickListener {
            // 인사하기 버튼 클릭 시 동작 설정
        }

        bottomSheet.setOnLikeClickListener {
            // 좋아요 버튼 클릭 시 동작 설정
        }

        bottomSheet.show(fragmentManager!!, bottomSheet.tag)
    }
}
