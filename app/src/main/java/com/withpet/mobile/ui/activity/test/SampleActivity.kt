package com.withpet.mobile.ui.test


import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.databinding.ActivitySampleBinding
import com.withpet.mobile.ui.activity.main.SomeoneList

class SampleActivity : BaseActivity() {
    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setList()
    }

    private fun setList() {
        val someoneList: SomeoneList = findViewById(R.id.someoneList)
        val someones = arrayOf(
            Someone("User1", "Address1", "Female", null),
            Someone("User2", "Address2", "Male", null),
            // 더 많은 Someone 객체 추가
        )
        someoneList.setSomeones(someones)
    }
}