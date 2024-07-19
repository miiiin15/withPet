package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.withpet.mobile.R


class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // 속성 초기화
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomButton)
            val textValue = typedArray.getString(R.styleable.CustomButton_android_text)
            typedArray.recycle()

            // 텍스트 설정
            text = textValue ?: "확인"
        }

        background = ContextCompat.getDrawable(context, R.drawable.selector_custom_button)
        setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    // isEnable 속성을 설정하는 함수
    fun setEnable(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }
}
