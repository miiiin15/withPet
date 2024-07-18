package com.withpet.mobile.ui.test


import android.os.Bundle
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.databinding.ActivitySampleBinding
import com.withpet.mobile.ui.activity.main.SomeoneList
import com.withpet.mobile.ui.custom.BottomNavigationBar
import com.withpet.mobile.utils.Logcat

class SampleActivity : BaseActivity() {
    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setList()

        val bottomNavigationBar: BottomNavigationBar = findViewById(R.id.bottomNavigationBar)
        bottomNavigationBar.selectedCategory = "홈"

        // 카테고리 클릭 리스너 설정 (필요 시)
        bottomNavigationBar.setOnClickListener {
            // TODO: 카테고리 클릭 시 동작 설정
            Logcat.d(bottomNavigationBar.selectedCategory)
        }
    }

    private fun setList() {
        val someoneList: SomeoneList = findViewById(R.id.someoneList)
        val someones = arrayOf(
            Someone("치와왕", "서울특별시 강남구", "Female", 25, null),
            Someone("무적폭풍 김모리", "서울특별시 강동구", "Female", 99, null),
            Someone("호날두", "부산광역시 해운대구", "Male", 22, null),
            Someone("민들레", "대구광역시 수성구", "Male", 28, null),
            Someone("닉넴뭐하지", "인천광역시 연수구", "Female", 27, null),
            Someone("유성 강타", "대전광역시 유성구", "Male", 26, null),
            Someone("xx12", "광주광역시 북구", "Female", 24, null),
            Someone("위즐리", "울산광역시 남구", "Male", 31, null),
            Someone("엔투소프트", "경기도 성남시 분당구", "Female", 29, null),
            Someone("창원케로베로스", "경상남도 창원시 의창구", "Male", 23, null)
        )

        someoneList.setSomeones(someones)
    }
}