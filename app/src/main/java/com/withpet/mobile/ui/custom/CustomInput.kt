package com.withpet.mobile.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

interface IsValidListener {
    fun isValid(text: String): Boolean
}

class CustomInput : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        // EditText의 배경을 투명한 밑줄로 설정
        background = ContextCompat.getDrawable(context, R.drawable.custom_input_bg)
        // inputType이 textpassword 타입이 아닌경우 1줄만
        isSingleLine = inputType != 129 && inputType != 128

        setTextAppearance(context, R.style.CustomInputTextStyle)
        setHintTextColor(ContextCompat.getColor(context,R.color.disable))

        // 입력 중, 올바른 값, 올바르지 않은 값에 따라 밑줄 색상을 변경
        setOnFocusChangeListener { _, hasFocus ->
            val colorResId = if (hasFocus) {

                R.color.primary // 기본 값
            } else if (isValid() && !hasFocus) {
                R.color.disable // 기본 값
            } else if (isValid() && hasFocus) {
                R.color.primary // 올바른 값
            } else {
                R.color.error // 올바르지 않은 값
            }
            setUnderlineColor(ContextCompat.getColor(context, colorResId))
        }
    }

    private fun setUnderlineColor(color: Int) {
        val layerDrawable = background as? LayerDrawable
        layerDrawable?.findDrawableByLayerId(R.id.underLine)?.apply {
            if (this is GradientDrawable) {
                setColor(color)
            }
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // 텍스트가 변경될 때 수행할 작업
        val text = s.toString()
        val colorResId = if (isValid(text)) {
            if (hasFocus()) {
                R.color.primary // 올바른 값
            } else {
                R.color.disable // 올바른 값
            }
        } else {
            R.color.disable // 올바르지 않은 값
        }
        setUnderlineColor(ContextCompat.getColor(context, colorResId))
    }

//    }

    private var isValidListener: IsValidListener? = null

    fun setIsValidListener(listener: IsValidListener) {
        isValidListener = listener
    }

    // 포커스용
    private fun isValid(): Boolean {
        val text = text.toString()
        return isValidListener?.isValid(text) ?: text.isNotEmpty() // listener가 설정되지 않으면 항상 true로 가정
    }

    //값 변화 검사용
    private fun isValid(text: String): Boolean {
        return isValidListener?.isValid(text) ?: text.isNotEmpty() // listener가 설정되지 않으면 항상 true로 가정
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        // disable 상태일 경우 전체 입력 필드의 색상 변경
        val colorResId = R.color.disable
        val color = ContextCompat.getColor(context, colorResId)
        setUnderlineColor(ContextCompat.getColor(context, colorResId))
    }
}
