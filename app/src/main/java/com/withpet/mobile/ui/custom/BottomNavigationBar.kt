package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.withpet.mobile.R
import com.withpet.mobile.data.enums.Category

class BottomNavigationBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {

    // 클릭 리스너 인터페이스 정의
    interface OnCategorySelectedListener {
        fun onCategorySelected(category: Category)
    }

    // 리스너를 저장할 변수
    private var listener: OnCategorySelectedListener? = null

    // 리스너를 설정하는 메서드
    fun setOnCategorySelectedListener(listener: OnCategorySelectedListener) {
        this.listener = listener
    }

    // 카테고리 상태값을 저장하는 변수
    private var _selectedCategory: Category = Category.MAIN
        set(value) {
            field = value
            updateNavigationBar()
        }

    init {
        View.inflate(context, R.layout.bottom_navigation_bar, this)
        // 클릭 리스너를 각 카테고리 레이아웃에 설정
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_chat).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_match).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.nav_walk).setOnClickListener(this)
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

            if (category == _selectedCategory) {
                iconView?.setColorFilter(ContextCompat.getColor(context, R.color.primary))
                textView?.setTextColor(ContextCompat.getColor(context, R.color.primary))
            } else {
                iconView?.setColorFilter(ContextCompat.getColor(context, R.color.disable))
                textView?.setTextColor(ContextCompat.getColor(context, R.color.disable))
            }
        }
    }

    fun getCategory(): Category {
        return _selectedCategory
    }

    fun setCategory(category: Category) {
        _selectedCategory = category
    }

    // 네비게이션 아이템 클릭 리스너
    override fun onClick(view: View) {
        when (view.id) {
            R.id.nav_home -> {
                if (_selectedCategory != Category.MAIN) {
                    _selectedCategory = Category.MAIN
                    listener?.onCategorySelected(_selectedCategory)
                }
            }

            R.id.nav_chat -> {
                if (_selectedCategory != Category.CHAT) {
                    _selectedCategory = Category.CHAT
                    listener?.onCategorySelected(_selectedCategory)
                }
            }

            R.id.nav_match -> {
                if (_selectedCategory != Category.MATCH) {
                    _selectedCategory = Category.MATCH
                    listener?.onCategorySelected(_selectedCategory)
                }
            }

            R.id.nav_walk -> {
                if (_selectedCategory != Category.WALK) {
                    _selectedCategory = Category.WALK
                    listener?.onCategorySelected(_selectedCategory)
                }
            }

            R.id.nav_my -> {
                if (_selectedCategory != Category.PROFILE) {
                    _selectedCategory = Category.PROFILE
                    listener?.onCategorySelected(_selectedCategory)
                }
            }
        }
    }
}
