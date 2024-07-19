package com.withpet.mobile.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

interface IsValidListener {
    fun isValid(text: String): Boolean
}

class CustomInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isValidListener: IsValidListener? = null

    init {
        setBackgroundResource(R.drawable.bg_custom_input)
//        setPadding(0, 20, 0, 20)
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setHintTextColor(ContextCompat.getColor(context, R.color.disable))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        isFocusable = true
        isFocusableInTouchMode = true

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
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        val colorResId = if (enabled) R.color.disable else R.color.disable
        setUnderlineColor(colorResId)

        val colorResIdBackground = if (enabled) R.color.transparent else R.color.disable
        setCustomBackgroundColor(colorResIdBackground)

        val hintTextColorResId = if (enabled) R.color.txt1 else R.color.txt3
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

    fun setDisable(disable: Boolean) {
        isEnabled = !disable
    }

    fun setIsValidListener(listener: IsValidListener) {
        isValidListener = listener
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

    private fun isValid(text: String): Boolean {
        return isValidListener?.isValid(text) ?: text.isNotEmpty()
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
