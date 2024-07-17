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
            Someone("김철수", "서울특별시 강남구", "Female", null),
            Someone("이영희", "서울특별시 서초구", "Male", null),
            Someone("박준영", "부산광역시 해운대구", "Female", null),
            Someone("최수지", "대구광역시 수성구", "Male", null),
            Someone("강민지", "인천광역시 연수구", "Female", null),
            Someone("윤서진", "대전광역시 유성구", "Male", null),
            Someone("정하나", "광주광역시 북구", "Female", null),
            Someone("오지훈", "울산광역시 남구", "Male", null),
            Someone("홍길동", "경기도 성남시 분당구", "Female", null),
            Someone("고수민", "경상남도 창원시 의창구", "Male", null)
        )
        someoneList.setSomeones(someones)
    }
}