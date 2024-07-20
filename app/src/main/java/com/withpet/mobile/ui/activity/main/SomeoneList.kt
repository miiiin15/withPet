package com.withpet.mobile.ui.activity.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.ui.custom.CustomLikeButton

// SomeoneList 컴포넌트 정의
class SomeoneList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val someoneAdapter = SomeoneAdapter()

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = someoneAdapter

        // PagerSnapHelper 추가
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    fun setSomeones(someones: Array<Someone>) {
        someoneAdapter.setSomeones(someones)
    }

    fun setOnItemClickListener(listener: (Someone) -> Unit) {
        someoneAdapter.setOnItemClickListener(listener)
    }

    // 어댑터 클래스 정의
    inner class SomeoneAdapter : RecyclerView.Adapter<SomeoneAdapter.SomeoneViewHolder>() {

        private var someones: Array<Someone> = arrayOf()
        private var itemClickListener: ((Someone) -> Unit)? = null

        fun setSomeones(someones: Array<Someone>) {
            this.someones = someones
            notifyDataSetChanged()
        }

        fun setOnItemClickListener(listener: (Someone) -> Unit) {
            this.itemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SomeoneViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_someone_list, parent, false)
            return SomeoneViewHolder(view)
        }

        override fun onBindViewHolder(holder: SomeoneViewHolder, position: Int) {
            val someone = someones[position]
            holder.bind(someone, position, itemCount)
        }

        override fun getItemCount(): Int = someones.size

        inner class SomeoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
            private val addressText: TextView = itemView.findViewById(R.id.addressText)
            private val usernameText: TextView = itemView.findViewById(R.id.usernameText)
            private val genderText: TextView = itemView.findViewById(R.id.genderText)
            private val ageText: TextView = itemView.findViewById(R.id.ageText)
            private val actionButton: CustomLikeButton = itemView.findViewById(R.id.likeButton)
            private val profileImage: ImageView =
                itemView.findViewById(R.id.profileImage) // 프로필 이미지뷰
            private val userInfoLayout: ViewGroup =
                itemView.findViewById(R.id.userInfoLayout) // 사용자 정보 레이아웃

            val displayMetrics: DisplayMetrics = itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val marginHorizontal =
                itemView.context.resources.getDimensionPixelSize(R.dimen.margin_horizontal)
            val cardWidth = screenWidth - marginHorizontal * 5

            init {
                // 화면 너비와 마진 값을 가져와서 MaterialCardView의 너비를 설정

                // cardView의 레이아웃 파라미터를 설정
                val layoutParams = cardView.layoutParams
                layoutParams.width = cardWidth
                layoutParams.height = cardWidth
                cardView.layoutParams = layoutParams
            }

            @SuppressLint("SetTextI18n")
            fun bind(someone: Someone, position: Int, itemCount: Int) {
                val ITEM_MARGIN = (screenWidth - cardWidth) / 8 // 아이템 간의 여백을 16dp로 설정

                addressText.text = someone.address
                usernameText.text = someone.username
                genderText.text = if (someone.gender == "Male") "남자" else "여자"
                ageText.text = "${someone.age} 세"

                // TODO: 프로필 이미지 설정
                // 예: Glide.with(itemView).load(someone.profileImage).into(profileImage)

                // TODO: actionButton click listener 설정
                actionButton.setOnClickListener {
                    actionButton.isLike = !actionButton.isLike
                }

                // 첫 번째 또는 마지막 항목인 경우 layout_marginRight 설정
                val params = cardView.layoutParams as ViewGroup.MarginLayoutParams
                val userInfoParams = userInfoLayout.layoutParams as ViewGroup.MarginLayoutParams
                val displayMetrics: DisplayMetrics = itemView.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val marginHorizontal = (screenWidth - cardView.layoutParams.width) / 2

                when (position) {
                    0 -> {
                        params.leftMargin = marginHorizontal
                        params.rightMargin = ITEM_MARGIN // 아이템 간의 여백 설정
                        userInfoParams.leftMargin = marginHorizontal
                        userInfoParams.rightMargin = ITEM_MARGIN
                    }
                    itemCount - 1 -> {
                        params.leftMargin = ITEM_MARGIN // 아이템 간의 여백 설정
                        params.rightMargin = marginHorizontal
                        userInfoParams.leftMargin = ITEM_MARGIN
                        userInfoParams.rightMargin = marginHorizontal
                    }
                    else -> {
                        params.leftMargin = ITEM_MARGIN // 아이템 간의 여백 설정
                        params.rightMargin = ITEM_MARGIN // 아이템 간의 여백 설정
                        userInfoParams.leftMargin = ITEM_MARGIN
                        userInfoParams.rightMargin = ITEM_MARGIN
                    }
                }
                cardView.layoutParams = params
                userInfoLayout.layoutParams = userInfoParams

                // 카드뷰 클릭 리스너 설정
                cardView.setOnClickListener {
                    itemClickListener?.invoke(someone)
                }
            }
        }
    }
}