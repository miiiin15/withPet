package com.withpet.mobile.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.R

data class SelectItem(val label: String, val value: String, var checked: Boolean = false)

class CustomSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var options: Array<SelectItem> = arrayOf()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var isDisabled: Boolean = false
    private var value: String = ""
    var type: String = "list"

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomSelect,
            0, 0
        ).apply {
            try {
                type = getString(R.styleable.CustomSelect_type) ?: "list"
            } finally {
                recycle()
            }
        }
        isCursorVisible = false

        setBackgroundResource(R.drawable.bg_custom_select)
//        setPadding(0, 20, 0, 20)
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setHintTextColor(ContextCompat.getColor(context, R.color.disable))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        setOnClickListener {
            if (!isDisabled) { // 클릭 시 isDisabled 확인
                showOptions()
            }
        }
    }

    fun setOptions(options: Array<SelectItem>) {
        this.options = options
        options.find { it.checked }?.label.let {
            setText(it)
        }
    }

    fun showOptions() {
        setUnderlineColor(R.color.primary)
        val bottomSheetView =
            LayoutInflater.from(context).inflate(R.layout.custom_bottom_sheet_container, null)
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)

        when (type) {
            "list" -> inflateListOptionsView(contentFrame)
            "gender" -> inflateGenderOptionsView(contentFrame)
            else -> {
                text = null
                value = ""
            }
        }

        setupBottomSheetDialog(bottomSheetView)
    }

    // 리스트 타입의 옵션을 설정하는 메서드
    private fun inflateListOptionsView(contentFrame: FrameLayout) {
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_select_list, null)
        contentFrame.addView(customView)

        val recyclerView: RecyclerView = customView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = OptionAdapter(options) { selectedOption ->
            options.forEach { it.checked = it == selectedOption }
            setText(selectedOption.label)
            value = selectedOption.value
            hidePopup()
        }
    }

    // 성별을 선택하는 옵션을 설정하는 메서드.
    private fun inflateGenderOptionsView(contentFrame: FrameLayout) {
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_view_gender, null)
        contentFrame.addView(customView)

        val cardView1: MaterialCardView = customView.findViewById(R.id.card_view_1)
        val cardView2: MaterialCardView = customView.findViewById(R.id.card_view_2)

        setCardViewClickListeners(cardView1, cardView2)
    }

    // 성별 선택 카드뷰의 클릭 이벤트 처리를 위한 메서드.
    private fun setCardViewClickListeners(cardView1: MaterialCardView, cardView2: MaterialCardView) {
        cardView1.setOnClickListener {
            setText("남성")
            value = "MALE"
            hidePopup()
        }
        cardView2.setOnClickListener {
            setText("여성")
            value = "FEMALE"
            hidePopup()
        }
    }

    // Bottom Sheet 다이얼로그를 설정하고 초기화하는 메서드.
    private fun setupBottomSheetDialog(bottomSheetView: View) {
        bottomSheetDialog = BottomSheetDialog(context).apply {
            setContentView(bottomSheetView)
            setOnShowListener {
                val parentLayout = bottomSheetView.parent as ViewGroup
                parentLayout.setBackgroundResource(android.R.color.transparent)
            }
            setOnCancelListener {
                setUnderlineColor(R.color.disable)
            }
        }

        setupBottomSheetBehavior(bottomSheetView)
        bottomSheetDialog.show()
    }

    // Bottom Sheet의 동작을 정의하는 메서드
    private fun setupBottomSheetBehavior(bottomSheetView: View) {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        val defaultHeight = (context.resources.displayMetrics.heightPixels * 0.4).toInt()
        bottomSheetBehavior.peekHeight = defaultHeight
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 이벤트 비활성화
            }
        })
    }


    // 선택된 옵션의 값에 접근하기 위한 메서드
    fun getValue(): String? {
        return value
    }

    private fun hidePopup() {
        setUnderlineColor(R.color.disable)
        bottomSheetDialog.dismiss()
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

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        // disable 상태일 경우 전체 입력 필드의 색상 변경
        val colorResId = if (enabled) R.color.txt1 else R.color.txt3
        setHintTextColor(ContextCompat.getColor(context, colorResId))

        val colorResIdBackground = if (enabled) R.color.transparent else R.color.disable
        setCustomBackgroundColor(colorResIdBackground)


        isDisabled = !enabled // 상태 저장
    }

    // disable 상태 설정 메서드
    fun setDisable(disable: Boolean) {
        isDisabled = disable
        setEnabled(!disable)
    }

    private inner class OptionAdapter(
        private val options: Array<SelectItem>,
        private val itemClickListener: (SelectItem) -> Unit
    ) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_option, parent, false)
            return OptionViewHolder(view)
        }

        override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
            val option = options[position]
            holder.bind(option)
        }

        override fun getItemCount(): Int = options.size

        inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val label: TextView = itemView.findViewById(R.id.option_label)

            fun bind(option: SelectItem) {
                label.text = option.label
                itemView.setOnClickListener { itemClickListener(option) }
            }
        }
    }
}
