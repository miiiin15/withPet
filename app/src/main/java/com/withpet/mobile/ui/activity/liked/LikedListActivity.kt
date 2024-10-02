package com.withpet.mobile.ui.activity.liked

import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.databinding.ActivityLikedBinding
import com.withpet.mobile.viewmodel.LikedViewModel

class LikedListActivity : BaseActivity() {

    private lateinit var binding: ActivityLikedBinding
    private lateinit var viewModel: LikedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLikedBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}