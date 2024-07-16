package com.withpet.mobile.ui.activity.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone

// SomeoneList 컴포넌트 정의
class SomeoneList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val someoneAdapter = SomeoneAdapter()

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = someoneAdapter
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
            holder.bind(someone)
        }

        override fun getItemCount(): Int = someones.size

        inner class SomeoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
            private val addressText: TextView = itemView.findViewById(R.id.addressText)
            private val usernameText: TextView = itemView.findViewById(R.id.usernameText)
            private val genderText: TextView = itemView.findViewById(R.id.genderText)
            private val actionButton: View = itemView.findViewById(R.id.actionButton)

            fun bind(someone: Someone) {
                addressText.text = someone.address
                usernameText.text = someone.username
                genderText.text = someone.gender

                // TODO: 프로필 이미지 설정
                // 예: Glide.with(itemView).load(someone.profileImage).into(imageView)

                // TODO: actionButton click listener 설정
                actionButton.setOnClickListener {
                    // 액션 버튼 클릭 리스너
                }
            }
        }
    }
}