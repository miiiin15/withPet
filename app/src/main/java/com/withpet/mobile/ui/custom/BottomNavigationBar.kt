package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

class BottomNavigationBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {

    // 카테고리 상태값을 저장하는 변수
    var selectedCategory: String = "홈"
        set(value) {
            field = value
            updateNavigationBar()
        }

    init {
        View.inflate(context, R.layout.bottom_navigation_bar, this)
        // 클릭 리스너를 각 카테고리 레이아웃에 설정
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_chat).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_walk).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_match).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_my).setOnClickListener(this)
        updateNavigationBar()
    }

    // 네비게이션 바 업데이트
    private fun updateNavigationBar() {
        val categories = listOf("홈", "채팅", "산책하기", "매칭", "MY")

        categories.forEach { category ->
            val iconView = findViewWithTag<ImageView>("${category}_icon")
            val textView = findViewWithTag<TextView>("${category}_text")

            if (category == selectedCategory) {
                iconView?.setColorFilter(ContextCompat.getColor(context, R.color.primary))
                textView?.setTextColor(ContextCompat.getColor(context, R.color.primary))
                // TODO: 선택된 상태의 아이콘 설정
            } else {
                iconView?.setColorFilter(ContextCompat.getColor(context, R.color.disable))
                textView?.setTextColor(ContextCompat.getColor(context, R.color.disable))
                // TODO: 선택되지 않은 상태의 아이콘 설정
            }
        }
    }

    // 네비게이션 아이템 클릭 리스너
    override fun onClick(view: View) {
        when (view.id) {
            R.id.nav_home -> {
                selectedCategory = "홈"
                Toast.makeText(context, "홈 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_chat -> {
                selectedCategory = "채팅"
                Toast.makeText(context, "채팅 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_walk -> {
                selectedCategory = "산책하기"
                Toast.makeText(context, "산책하기 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_match -> {
                selectedCategory = "매칭"
                Toast.makeText(context, "매칭 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my -> {
                selectedCategory = "MY"
                Toast.makeText(context, "MY 클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }
}