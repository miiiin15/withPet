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
import com.withpet.mobile.data.enums.Category

class BottomNavigationBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {

    // 카테고리 상태값을 저장하는 변수
    var selectedCategory: Category = Category.MAIN
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
        val categories = mapOf(
            Category.MAIN to "MAIN",
            Category.CHAT to "CHAT",
            Category.WALK to "WALK",
            Category.MATCH to "MATCH",
            Category.PROFILE to "PROFILE"
        )

        categories.forEach { (category, tag) ->
            val iconView = findViewWithTag<ImageView>("${tag}_icon")
            val textView = findViewWithTag<TextView>("${tag}_text")

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
                selectedCategory = Category.MAIN
                Toast.makeText(context, "MAIN 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_chat -> {
                selectedCategory = Category.CHAT
                Toast.makeText(context, "CHAT 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_walk -> {
                selectedCategory = Category.WALK
                Toast.makeText(context, "WALK 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_match -> {
                selectedCategory = Category.MATCH
                Toast.makeText(context, "MATCH 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my -> {
                selectedCategory = Category.PROFILE
                Toast.makeText(context, "PROFILE 클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
