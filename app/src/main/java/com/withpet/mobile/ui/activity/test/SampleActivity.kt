package com.withpet.mobile.ui.test


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.databinding.ActivitySampleBinding
import com.withpet.mobile.ui.activity.main.SomeoneList
import com.withpet.mobile.ui.custom.BottomNavigationBar
import com.withpet.mobile.utils.Logcat

class SampleActivity : BaseActivity() {
    private lateinit var binding: ActivitySampleBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setList()
        setBottomSheet()

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
            Someone("엔투소프트", "경기도 성남시 중원구", "Female", 29, null),
            Someone("창원케로베로스", "경상남도 창원시 의창구", "Male", 23, null)
        )

        someoneList.setSomeones(someones)
        someoneList.setOnItemClickListener { someone ->
            Toast.makeText(this, "${someone.username} 클릭됨", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.show()
        }
    }

    private fun setBottomSheet() {
        val bottomSheetView =
            LayoutInflater.from(this).inflate(R.layout.custom_bottom_sheet_container, null)
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_view_someone_info, null)
        contentFrame.addView(customView)

        // Bottom sheet dialog 설정
        bottomSheetDialog = BottomSheetDialog(this).apply {
            setContentView(bottomSheetView)
            setOnShowListener {
                val parentLayout = bottomSheetView.parent as ViewGroup
                parentLayout.setBackgroundResource(android.R.color.transparent)
            }
            setOnCancelListener {
            }
        }

        // Bottom sheet behavior 초기화
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
//        val defaultHeight = (this.resources.displayMetrics.heightPixels * 0.4).toInt()
//        bottomSheetBehavior.peekHeight = defaultHeight
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED // 초기 높이를 동일하게 설정
        bottomSheetBehavior.isHideable = false // 슬라이드 비활성화

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // STATE_COLLAPSED 외의 상태로 변경되지 않도록 처리
                if (newState != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 이벤트 비활성화
            }
        })

        // Bottom sheet 크기 조정
//        bottomSheetView.layoutParams.height = defaultHeight

    }
}