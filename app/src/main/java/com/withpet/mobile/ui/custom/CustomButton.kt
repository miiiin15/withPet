package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

enum class ButtonType {
    NORMAL,
    WHITE
}

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var buttonType: ButtonType = ButtonType.NORMAL

    init {
        init(attrs)
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0).apply {
            try {
                val type = getString(R.styleable.CustomButton_buttonType) ?: "NORMAL"
                val buttonType = runCatching { enumValueOf<ButtonType>(type.toUpperCase()) }
                    .getOrElse { ButtonType.NORMAL }

                setButtonType(buttonType)
            } finally {
                recycle()
            }
        }
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomButton)
            val textValue = typedArray.getString(R.styleable.CustomButton_android_text)
            typedArray.recycle()

            // 텍스트 설정
            setTextStyle(textValue)
        }
        applyButtonDesign()
    }


    private fun setTextStyle(textValue: String?) {
        text = textValue ?: "확인"
        textSize = 16F
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    fun setEnable(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    fun setButtonType(type: ButtonType) {
        buttonType = type
        applyButtonDesign()
    }

    private fun applyButtonDesign() {
        when (buttonType) {
            ButtonType.NORMAL -> {
                background = ContextCompat.getDrawable(context, R.drawable.selector_custom_button)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            ButtonType.WHITE -> {
                background =
                    ContextCompat.getDrawable(context, R.drawable.selector_custom_button_white)
                setTextColor(ContextCompat.getColor(context, R.color.primary))
            }
            else -> {
                background = ContextCompat.getDrawable(context, R.drawable.selector_custom_button)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }
}
