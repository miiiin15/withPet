package com.withpet.mobile.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
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
import com.withpet.mobile.R

data class Option(val label: String, val value: String, var checked: Boolean = false)

class CustomSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var options: Array<Option> = arrayOf()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var type: String = "list"

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

        background = ContextCompat.getDrawable(context, R.drawable.custom_select_bg)
        setOnClickListener { showOptions() }
    }

    fun setOptions(options: Array<Option>) {
        this.options = options
        setText(options.find { it.checked }?.label ?: context.getString(R.string.select_prompt))
    }

    private fun showOptions() {
        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_container, null)
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_select_list, null)
        contentFrame.addView(customView)

        val recyclerView: RecyclerView = customView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        when (type) {
            "list" -> {
                recyclerView.adapter = OptionAdapter(options) { selectedOption ->
                    options.forEach { it.checked = it == selectedOption }
                    setText(selectedOption.label)
                    hidePopup()
                }
            }
            "A" -> {
                // Type A에 대한 처리
            }
            "B" -> {
                // Type B에 대한 처리
            }
            "C" -> {
                // Type C에 대한 처리
            }
            else -> {
                recyclerView.adapter = OptionAdapter(options) { selectedOption ->
                    options.forEach { it.checked = it == selectedOption }
                    setText(selectedOption.label)
                    hidePopup()
                }
            }
        }

        // Bottom sheet dialog 설정
        bottomSheetDialog = BottomSheetDialog(context).apply {
            setContentView(bottomSheetView)
            setOnShowListener {
                val parentLayout = bottomSheetView.parent as ViewGroup
                parentLayout.setBackgroundResource(android.R.color.transparent)
            }
        }

        // Bottom sheet behavior 초기화
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        val defaultHeight = (context.resources.displayMetrics.heightPixels * 0.4).toInt()
        bottomSheetBehavior.peekHeight = defaultHeight
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED // 초기 높이를 동일하게 설정
        bottomSheetBehavior.isHideable = false // 슬라이드 비활성화

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // STATE_COLLAPSED 외의 상태로 변경되지 않도록 처리
                if (newState != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 이벤트 비활성화
            }
        })

        // Bottom sheet 크기 조정
        bottomSheetView.layoutParams.height = defaultHeight

        bottomSheetDialog.show()
    }

    // 선택된 옵션의 값에 접근하기 위한 메서드
    fun getValue(): String? {
        return options.find { it.checked }?.value
    }

    private fun hidePopup() {
        bottomSheetDialog.dismiss()
    }

    private fun setUnderlineColor(color: Int) {
        val layerDrawable = background as? LayerDrawable
        layerDrawable?.findDrawableByLayerId(R.id.underLine)?.apply {
            if (this is GradientDrawable) {
                setColor(color)
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        // disable 상태일 경우 전체 입력 필드의 색상 변경
        val colorResId = R.color.disable
        val color = ContextCompat.getColor(context, colorResId)
        setUnderlineColor(ContextCompat.getColor(context, colorResId))
    }

    private inner class OptionAdapter(
        private val options: Array<Option>,
        private val itemClickListener: (Option) -> Unit
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

            fun bind(option: Option) {
                label.text = option.label
                itemView.setOnClickListener { itemClickListener(option) }
            }
        }
    }
}
