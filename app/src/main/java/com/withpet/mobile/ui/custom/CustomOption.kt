package com.withpet.mobile.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.withpet.mobile.R

class CustomOption @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val customSelect: CustomSelect
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
        // CustomSelect (AppCompatEditText 기반 컴포넌트)을 초기화
        customSelect = CustomSelect(context, attrs, defStyleAttr)

        // label을 상단에 추가
        addView(label, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ))

        // CustomSelect를 가운데 추가
        addView(customSelect, LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ))

        // errorText를 하단에 추가
        addView(errorText, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ))

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

        // CustomSelect의 클릭 리스너 등을 설정할 수 있음
        customSelect.setOnClickListener {
            if(!customSelect.isDisabled){
            customSelect.showOptions()
            }
        }
    }

    // CustomSelect의 type 속성에 접근할 수 있는 프로퍼티 추가
    var type: String?
        get() = customSelect.type
        set(value) {
            if (value != null) {
                customSelect.type = value
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

    // CustomSelect의 상호작용을 위한 메서드들 래핑
    override fun setOnClickListener(listener: OnClickListener?) {
        customSelect.setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        // customEditText의 visibility를 CustomInput의 visibility와 동일하게 설정
        customSelect.visibility = visibility
    }

    override fun getVisibility(): Int {
        return super.getVisibility()
    }

    fun getSelectedValue(): String? {
        return customSelect.text.toString()  // CustomSelect에 저장된 선택된 값을 반환
    }

    fun setDisable(disable: Boolean) {
        customSelect.setDisable(disable)
    }

    // CustomSelect의 setOptions 메서드를 연결하는 메서드 추가
    fun setOptions(options: Array<SelectItem>) {
        customSelect.setOptions(options)
    }

    fun getValue(): String? {
        return customSelect.getValue()
    }
}