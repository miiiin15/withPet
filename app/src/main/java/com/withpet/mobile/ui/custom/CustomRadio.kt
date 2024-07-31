package com.withpet.mobile.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

data class RadioItem(val label: String, val value: String, var checked: Boolean = false)

class CustomRadio @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var options: Array<RadioItem> = emptyArray()
    private var maxButtonsPerRow: Int = 2
    private var allowMultipleSelection: Boolean = false
    private var value: String = ""

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.custom_radio, this, true)
    }

    fun setOptions(
        options: Array<RadioItem>,
        maxButtonsPerRow: Int = 2,
        allowMultipleSelection: Boolean = false
    ) {
        this.options = options
        this.maxButtonsPerRow = maxButtonsPerRow
        this.allowMultipleSelection = allowMultipleSelection
        renderOptions()
    }

    // 선택된 옵션의 값에 접근하기 위한 메서드
    fun getValue(): String? {
        return value
    }


    private fun renderOptions() {
        removeAllViews()

        val rowCount = (options.size + maxButtonsPerRow - 1) / maxButtonsPerRow

        for (i in 0 until rowCount) {
            val row = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }

            for (j in 0 until maxButtonsPerRow) {
                val index = i * maxButtonsPerRow + j
                if (index < options.size) {
                    val option = options[index]
                    val optionView = TextView(context).apply {
                        text = option.label
                        textAlignment = TEXT_ALIGNMENT_CENTER
                        textSize = 14f
                        setTypeface(typeface, android.graphics.Typeface.BOLD)
                        tag = option.value
                        layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
                            if (index % 2 == 1) setMargins(24, 0, 0, 0) else setMargins(0, 0, 24, 0)
                        }
                        setPadding(36, 36, 36, 36)
                        setBackgroundResource(if (option.checked) R.drawable.bg_radio_enable else R.drawable.bg_radio_disable)
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                if (option.checked) R.color.primary else R.color.disable
                            )
                        )
                        setOnClickListener {
                            handleOptionClick(this, option)
                        }
                    }
                    row.addView(optionView)
                } else {
                    // 빈 공간을 채우기 위해 보이지 않는 View 추가
                    val emptyView = View(context).apply {
                        layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                    }
                    row.addView(emptyView)
                }
            }
            addView(row)
        }
    }

    private fun handleOptionClick(view: TextView, option: RadioItem) {
        if (allowMultipleSelection) {
            option.checked = !option.checked
            view.setBackgroundResource(if (option.checked) R.drawable.bg_button_enable else R.drawable.bg_button_disable)
        } else {
            if (option.checked) {
                option.checked = false
                // TODO : allowMultipleSelection else 다중 선택 케이스 작성하기
                if (!allowMultipleSelection) value = option.value
                view.setBackgroundResource(R.drawable.bg_button_disable)
            } else {
                options.forEach {
                    it.checked = false
                }
                option.checked = true
                renderOptions()
            }
        }
    }
}
