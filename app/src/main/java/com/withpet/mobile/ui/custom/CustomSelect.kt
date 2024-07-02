package com.withpet.mobile.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.withpet.mobile.R


data class Option(val label: String, val value: String, var checked: Boolean = false)

class CustomSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var options: Array<Option> = arrayOf()
    private lateinit var bottomSheetDialog: BottomSheetDialog

    init {
        setOnClickListener { showOptions() }
    }

    fun setOptions(options: Array<Option>) {
        this.options = options
        setText(options.find { it.checked }?.label ?: context.getString(R.string.select_prompt))
    }

    private fun showOptions() {
        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_container, null)
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_select_list, null)
        contentFrame.addView(customView)

        // Bottom sheet dialog 설정
        bottomSheetDialog = BottomSheetDialog(context).apply {
            setContentView(bottomSheetView)
            setOnShowListener {
                val parentLayout = bottomSheetView.parent as ViewGroup
                parentLayout.setBackgroundResource(android.R.color.transparent)
            }
        }

        // Bottom sheet behavior 초기화
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hidePopup()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // TODO : 슬라이드 이벤트 처리 필요시 추가
            }
        })

        bottomSheetDialog.show()
    }

    // 선택된 옵션의 값에 접근하기 위한 메서드
    fun getValue(): String? {
        return options.find { it.checked }?.value
    }

    private fun hidePopup() {
        bottomSheetDialog.dismiss()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        val colorResId = if (enabled) R.color.primary else R.color.disable
        setTextColor(ContextCompat.getColor(context, colorResId))
    }
}
