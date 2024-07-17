package com.withpet.mobile.ui.activity.main

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.utils.Logcat

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

    // 어댑터 클래스 정의
    inner class SomeoneAdapter : RecyclerView.Adapter<SomeoneAdapter.SomeoneViewHolder>() {

        private var someones: Array<Someone> = arrayOf()

        fun setSomeones(someones: Array<Someone>) {
            this.someones = someones
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SomeoneViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.someone_list_item, parent, false)
            return SomeoneViewHolder(view)
        }

        override fun onBindViewHolder(holder: SomeoneViewHolder, position: Int) {
            val someone = someones[position]
            holder.bind(someone, position)
        }

        override fun getItemCount(): Int = someones.size

        inner class SomeoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
            private val infoLayout: LinearLayout = itemView.findViewById(R.id.userInfoLayout)
            private val addressText: TextView = itemView.findViewById(R.id.addressText)
            private val usernameText: TextView = itemView.findViewById(R.id.usernameText)
            private val genderText: TextView = itemView.findViewById(R.id.genderText)
            private val likeButton: Button = itemView.findViewById(R.id.likeButton)

            init {
                // 화면 너비와 마진 값을 가져와서 MaterialCardView의 너비를 설정
                val displayMetrics: DisplayMetrics = itemView.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val marginHorizontal =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.margin_horizontal)
                val cardWidth = screenWidth - marginHorizontal * 4

                // cardView의 레이아웃 파라미터를 설정
                val layoutParams = cardView.layoutParams
                layoutParams.width = cardWidth
                layoutParams.height = cardWidth
                cardView.layoutParams = layoutParams
            }

            fun bind(someone: Someone, position: Int) {
                addressText.text = someone.address
                usernameText.text = someone.username
                genderText.text = if (someone.gender == "Male") "남자" else "여자"
//                likeButton.isLike = false

                // TODO: 프로필 이미지 설정
                // 예: Glide.with(itemView).load(someone.profileImage).into(imageView)

                // TODO: actionButton click listener 설정
                likeButton.setOnClickListener {
                    // 액션 버튼 클릭 리스너
                }

                // TODO : 리스트 간격 문제 해결하기
                // 마지막 항목인 경우 layout_marginRight 설정
                val cardViewParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
                val infoLayoutParams = infoLayout.layoutParams as ViewGroup.MarginLayoutParams
                val marginHorizontal =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.margin_horizontal)
                if (position == 0) {
//                    Logcat.d("나느 $position")
                    cardViewParams.leftMargin = marginHorizontal *2
                    infoLayoutParams.leftMargin = marginHorizontal *2
                } else {
                    cardViewParams.leftMargin = 0
                    infoLayoutParams.leftMargin = 0
                }

                if (position == itemCount - 1) {
                    Logcat.d("이  $position")
                    cardViewParams.rightMargin = marginHorizontal*2
                } else {
                    cardViewParams.rightMargin = 0
                }
                cardView.layoutParams = cardViewParams
            }


        }
    }
}