package com.withpet.mobile.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

interface IsValidListener {
    fun isValid(text: String): Boolean
}

open class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isInitialized = false
    private var isValidListener: IsValidListener? = null

    init {
        setBackgroundResource(R.drawable.bg_custom_input)
//        setPadding(0, 20, 0, 20)
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setHintTextColor(ContextCompat.getColor(context, R.color.disable))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        setEnabled(true)
        isFocusable = true
        isFocusableInTouchMode = true
        isSingleLine = true

        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setUnderlineColor(R.color.primary)
            } else {
                if (!isValid()) {
                    setUnderlineColor(R.color.error)
                } else {
                    setUnderlineColor(R.color.disable)
                }
            }
        }

        // inputType이 textPassword, numberPassword 또는 number일 경우에도 초기 설정 적용
        if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
            inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) ||
            inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
            inputType == (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD) ||
            inputType == InputType.TYPE_CLASS_NUMBER
        ) {
            setInitialUnderlineColor()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        val colorResId = if (enabled) R.color.disable else R.color.disable
        setUnderlineColor(colorResId)

        val colorResIdBackground = if (enabled) R.color.transparent else R.color.disable
        setCustomBackgroundColor(colorResIdBackground)

        val hintTextColorResId = if (enabled) R.color.disable else R.color.txt3
        setHintTextColor(ContextCompat.getColor(context, hintTextColorResId))
    }

    override fun setError(error: CharSequence?) {
        super.setError(error)
        if (error != null) {
            setUnderlineColor(R.color.error)
        } else {
            setUnderlineColor(R.color.disable)
        }
    }

    // visibility를 외부에서 접근할 수 있도록 getter와 setter 메서드 오버라이드
    override fun getVisibility(): Int {
        return super.getVisibility()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        // 필요하면 추가적인 동작을 여기에 추가할 수 있음
    }

    fun setDisable(disable: Boolean) {
        isEnabled = !disable
    }

    fun setIsValidListener(listener: IsValidListener) {
        this.isValidListener = listener
        if (!isInitialized) {
            // 초기화 후 처음 한 번만 언더라인 색상을 설정
            setUnderlineColor(R.color.disable)
            isInitialized = true
        }
    }

    private fun isValid(): Boolean {
        val text = text.toString()
        return isValidListener?.isValid(text) ?: text.isNotEmpty()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s.toString()
        if (isValid(text)) {
            setUnderlineColor(R.color.primary)
        } else {
            setUnderlineColor(R.color.error)
        }
    }

    var onValidListener: ((Boolean) -> Unit)? = null

    private fun isValid(text: String): Boolean {
        onValidListener?.invoke(isValidListener?.isValid(text) ?: text.isNotEmpty())
        return isValidListener?.isValid(text) ?: text.isNotEmpty()
    }

    private fun setInitialUnderlineColor() {
        if (!isInitialized) {
            setUnderlineColor(R.color.disable)
            isInitialized = true
        }
    }

    private fun setUnderlineColor(colorResId: Int) {
        val drawable = background
        if (drawable is LayerDrawable) {
            val underline = drawable.findDrawableByLayerId(R.id.underLine) as? GradientDrawable
            underline?.setColor(ContextCompat.getColor(context, colorResId))
        } else {
            background?.mutate()?.setTint(ContextCompat.getColor(context, colorResId))
        }
    }

    private fun setCustomBackgroundColor(colorResId: Int) {
        val drawable = background
        if (drawable is LayerDrawable) {
            val underline = drawable.findDrawableByLayerId(R.id.background) as? GradientDrawable
            underline?.setColor(ContextCompat.getColor(context, colorResId))
        } else {
            background?.mutate()?.setTint(ContextCompat.getColor(context, colorResId))
        }
    }
}
