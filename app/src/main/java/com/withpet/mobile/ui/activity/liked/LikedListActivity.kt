package com.withpet.mobile.ui.activity.liked

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.databinding.ActivityLikedBinding
import com.withpet.mobile.ui.custom.LikedList
import com.withpet.mobile.viewmodel.LikedViewModel

class LikedListActivity : BaseActivity() {
    private val viewModel: LikedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data Binding 설정
        val binding: ActivityLikedBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_liked)

        // ViewModel 및 생명 주기 설정
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // 특정 이벤트에서 headerText 업데이트
        viewModel.updateHeaderTitle("좋아요 목록")

        // onCreateView에서 데이터가 이미 로드되지 않았는지 확인
        if (!viewModel.isDataLoaded) {
            viewModel.fetchLikedList()
        }

        viewModel.likedList.observe(this) { responce ->

            responce?.payload?.let { mateList ->

                if (mateList.isNotEmpty()) {
                    binding.emptyText.visibility = View.GONE
                } else {
                    binding.emptyText.visibility = View.VISIBLE
                }

                val likedList: LikedList = binding.likedList
                likedList.setLikedList(mateList)
                likedList.setOnItemClickListener { mate ->

                    // TODO : 좋아요한 목록 클릭 리스너
                }
            }
        }

        viewModel.error.observe(this, Observer { errorMsg ->
            showAlert(errorMsg)
        })

        viewModel.failure.observe(this, Observer { throwable ->
            showAlert(throwable.message ?: "Unknown error")
        })

    }
}