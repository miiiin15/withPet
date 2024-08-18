package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

class CustomInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val customEditText: CustomEditText
    private val label: AppCompatTextView
    private val errorText: AppCompatTextView

    init {
        orientation = VERTICAL

        // label과 errorText 초기화
        label = AppCompatTextView(context).apply {
            visibility = GONE
            textSize = 12f
            setTextColor(ContextCompat.getColor(context, R.color.label))
        }
        errorText = AppCompatTextView(context).apply {
            visibility = GONE
            textSize = 12f
            setTextColor(ContextCompat.getColor(context, R.color.error))
        }

        // CustomInput (AppCompatEditText 상속)을 초기화
        customEditText = CustomEditText(context, attrs, defStyleAttr)

        // label을 상단에 추가
        addView(
            label, LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // CustomInput을 가운데 추가
        addView(
            customEditText, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // errorText를 하단에 추가
        addView(
            errorText, LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // XML에서 정의된 속성을 읽어옴
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomEditText,
            0, 0
        ).apply {
            try {
                val labelText = getString(R.styleable.CustomEditText_labelText)
                val errorTextValue = getString(R.styleable.CustomEditText_errorText)

                setLabel(labelText)
                setErrorText(errorTextValue)
            } finally {
                recycle()
            }
        }
    }

    // label 설정 메서드
    fun setLabel(text: String?) {
        if (!text.isNullOrEmpty()) {
            label.text = text
            label.visibility = VISIBLE
        } else {
            label.visibility = GONE
        }
    }

    // errorText 설정 메서드
    fun setErrorText(text: String?) {
        if (!text.isNullOrEmpty()) {
            errorText.text = text
            errorText.visibility = VISIBLE
        } else {
            errorText.visibility = GONE
        }
    }

    // text 속성을 통해 editText의 텍스트에 바로 접근할 수 있게 함
    var text: CharSequence?
        get() = customEditText.text
        set(value) {
            customEditText.setText(value)
        }

    // CustomInput(AppCompatEditText)의 메서드를 제공하는 래퍼 메서드
    fun setHintTextColor(color: Int) {
        customEditText.setHintTextColor(color)
    }

    // CustomInput의 setIsValidListener를 호출하는 메서드
    fun setIsValidListener(listener: IsValidListener) {
        customEditText.setIsValidListener(listener)
    }

    fun setDisable(disable: Boolean) {
        customEditText.setDisable(disable)
    }
}
