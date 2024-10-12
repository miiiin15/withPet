package com.withpet.mobile.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone

class LikedList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    interface OnLikeRequestSuccess {
        fun onLikeRequestSuccess(currentLike: Boolean, success: Boolean)
    }
    private var likeButtonClickListener: OnLikeRequestSuccess? = null
    private var viewHolder: LikedListAdapter.LikedListViewHolder? = null
    private val someoneAdapter = LikedListAdapter()

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = someoneAdapter

        // PagerSnapHelper 추가
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    fun setLikedList(someones: List<Someone>) {
        someoneAdapter.setSomeones(someones)
    }

    fun setOnItemClickListener(listener: (Someone) -> Unit) {
        someoneAdapter.setOnItemClickListener(listener)
    }

    // 클릭 리스너를 외부에서 설정할 수 있는 메서드
    fun setOnLikeButtonClickListener(listener: OnLikeRequestSuccess) {
        likeButtonClickListener = listener
    }

    // 어댑터 클래스 정의
    inner class LikedListAdapter : RecyclerView.Adapter<LikedListAdapter.LikedListViewHolder>() {

        private var someones: List<Someone> = emptyList()
        private var itemClickListener: ((Someone) -> Unit)? = null

        fun setSomeones(someones: List<Someone>) {
            this.someones = someones
            notifyDataSetChanged()
        }

        fun setOnItemClickListener(listener: (Someone) -> Unit) {
            this.itemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_someone_list, parent, false)
            return LikedListViewHolder(view)
        }

        override fun onBindViewHolder(holder: LikedListViewHolder, position: Int) {
            val someone = someones[position]
            holder.bind(someone, position, itemCount)
        }

        override fun getItemCount(): Int = someones.size

        inner class LikedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val itemView: LinearLayout = itemView.findViewById(R.id.likedItemView)
            private val addressText: TextView = itemView.findViewById(R.id.liked_addressText)
            private val usernameText: TextView = itemView.findViewById(R.id.liked_usernameText)
            private val ageText: TextView = itemView.findViewById(R.id.liked_ageText)
            private val profileImage: ImageView =
                itemView.findViewById(R.id.liked_profileImage) // 프로필 이미지뷰


            init {

            }

            @SuppressLint("SetTextI18n")
            fun bind(someone: Someone, position: Int, itemCount: Int) {

                addressText.text = someone.regionName
                usernameText.text = someone.nickName
                ageText.text = "${someone.age}세"

                // TODO: 프로필 이미지 설정
                // 예: Glide.with(itemView).load(someone.profileImage).into(profileImage)

                // TODO : 삭제 버튼 리스너
//                deleteButton.setOnClickListener {
//                }



                // 카드뷰 클릭 리스너 설정
                itemView.setOnClickListener {
                    itemClickListener?.invoke(someone)
                }
            }

        }
    }
}
