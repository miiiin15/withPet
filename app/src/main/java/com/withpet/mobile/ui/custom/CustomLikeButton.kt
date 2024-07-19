package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import com.withpet.mobile.R

class CustomLikeButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {

    var isLike: Boolean = false
        set(value) {
            field = value
            refreshDrawableState()
            updateButtonState()
        }

    init {
        background = context.getDrawable(R.drawable.selector_like_button)
        updateButtonState()
        setOnClickListener {
            isLike = !isLike
        }
    }

    private fun updateButtonState() {
        isSelected = isLike
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isLike) {
            mergeDrawableStates(drawableState, STATE_LIKED)
        }
        return drawableState
    }

    companion object {
        private val STATE_LIKED = intArrayOf(android.R.attr.state_selected)
    }
}