package com.withpet.mobile.ui.test


import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.databinding.ActivitySampleBinding

class SampleActivity : BaseActivity() {
    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}