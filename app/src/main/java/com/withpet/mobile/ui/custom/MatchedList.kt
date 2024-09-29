package com.withpet.mobile.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.R
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.utils.Constants
import com.withpet.mobile.utils.Logcat
import java.lang.Exception

class MatchedList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    interface OnLikeRequestSuccess {
        fun onLikeRequestSuccess(currentLike: Boolean, success: Boolean)
    }

    private var likeButtonClickListener: OnLikeRequestSuccess? = null
    private val someoneAdapter = MatchedListAdapter()

    init {
        // GridLayoutManager를 사용하여 한 줄에 2개의 아이템 배치
        layoutManager = GridLayoutManager(context, 2) // spanCount = 2
        adapter = someoneAdapter
    }

    fun setSomeones(someones: List<Someone>) {
        someoneAdapter.setSomeones(someones)
    }

    fun setOnItemClickListener(listener: (Someone) -> Unit) {
        someoneAdapter.setOnItemClickListener(listener)
    }

    fun setOnLikeButtonClickListener(listener: OnLikeRequestSuccess) {
        likeButtonClickListener = listener
    }

    inner class MatchedListAdapter :
        RecyclerView.Adapter<MatchedListAdapter.MatchedListViewHolder>() {

        private var someones: List<Someone> = emptyList()
        private var itemClickListener: ((Someone) -> Unit)? = null

        fun setSomeones(someones: List<Someone>) {
            this.someones = someones
            notifyDataSetChanged()
        }

        fun setOnItemClickListener(listener: (Someone) -> Unit) {
            this.itemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchedListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_matched_list, parent, false)
            return MatchedListViewHolder(view)
        }

        override fun onBindViewHolder(holder: MatchedListViewHolder, position: Int) {
            val someone = someones[position]
            holder.bind(someone, position, someones.size)
        }

        override fun getItemCount(): Int = someones.size

        fun setLikeState(actionButton: CustomLikeButton, isLike: Boolean) {
            actionButton.isLike = isLike
        }

        inner class MatchedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: MaterialCardView = itemView.findViewById(R.id.match_cardView)
            private val addressText: TextView = itemView.findViewById(R.id.match_addressText)
            private val usernameText: TextView = itemView.findViewById(R.id.match_usernameText)
            private val ageText: TextView = itemView.findViewById(R.id.match_ageText)
            private val actionButton: CustomLikeButton =
                itemView.findViewById(R.id.match_likeButton)
            private val profileImage: ImageView = itemView.findViewById(R.id.match_profileImage)

            init {
                // 아이템의 기본 크기 설정 (화면의 40%로)
                val displayMetrics = itemView.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val itemSize = (screenWidth * 0.4).toInt() // 아이템 크기를 화면의 40%로 설정

                // 아이템의 크기 및 기본 여백 설정
                val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.width = itemSize
                layoutParams.height = itemSize
                cardView.layoutParams = layoutParams
            }

            @SuppressLint("SetTextI18n")
            fun bind(someone: Someone, position: Int, itemCount: Int) {
                addressText.text = someone.regionName
                usernameText.text = someone.nickName
                ageText.text = "${someone.age}세"
                actionButton.isLike = someone.like

                val fullImageUrl =
                    Constants.IMAGE_URL + "media" + someone.profileImage?.replace("\\", "/")
                Glide.with(itemView).load(fullImageUrl).into(profileImage)

                actionButton.setOnClickListener {
                    requestLike(
                        memberId = someone.memberId.toString(),
                        requestSuccess = { isSuccess ->
                            likeButtonClickListener?.onLikeRequestSuccess(
                                actionButton.isLike,
                                isSuccess
                            )
                        }
                    )
                }

                // 화면 크기를 사용하여 정확한 여백 계산
                val displayMetrics = itemView.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val itemSize = (screenWidth * 0.4).toInt()
                val remainingSpace = screenWidth - (itemSize * 2)
                val itemMargin = remainingSpace / 3 // 각 아이템 간격을 동일하게 유지하기 위해 3으로 나눔

                val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
                val addressTextLayoutParams =
                    addressText.layoutParams as ViewGroup.MarginLayoutParams
                val usernameTextLayoutParams =
                    usernameText.layoutParams as ViewGroup.MarginLayoutParams

                // 첫 번째 줄의 왼쪽 아이템 또는 첫 번째 아이템
                if (position == 0) {
                    layoutParams.leftMargin = itemMargin // 첫 아이템의 왼쪽 여백 설정
                    layoutParams.rightMargin = itemMargin / 2 // 중간 아이템 간격은 절반으로 설정

                    addressTextLayoutParams.leftMargin = itemMargin
                    usernameTextLayoutParams.leftMargin = itemMargin
                }
                // 짝수번째 아이템 (오른쪽 아이템)
                else if ((position + 1) % 2 == 0) {
                    layoutParams.leftMargin = itemMargin / 2 // 짝수 아이템은 왼쪽 간격을 절반으로 설정
                    layoutParams.rightMargin = itemMargin // 오른쪽 여백을 일반적으로 설정

                    addressTextLayoutParams.leftMargin = itemMargin / 2
                    usernameTextLayoutParams.leftMargin = itemMargin / 2
                }
                // 홀수번째 아이템 (왼쪽 아이템)
                else {
                    layoutParams.leftMargin = itemMargin // 홀수 아이템은 왼쪽 여백 설정
                    layoutParams.rightMargin = itemMargin / 2 // 중간 아이템의 오른쪽 간격을 절반으로 설정

                    addressTextLayoutParams.leftMargin = itemMargin
                    usernameTextLayoutParams.leftMargin = itemMargin
                }

                // 마지막 줄의 오른쪽 아이템이면 오른쪽 여백 조정
                if (position == itemCount - 1 && (position + 1) % 2 == 0) {
                    layoutParams.rightMargin = itemMargin // 마지막 아이템 오른쪽 여백 설정
                }

                cardView.layoutParams = layoutParams

                cardView.setOnClickListener {
                    itemClickListener?.invoke(someone)
                }
            }

            fun requestLike(memberId: String, requestSuccess: (isSuccess: Boolean) -> Unit?) {
                try {
                    if (!actionButton.isLike) {
                        CommonRepo.sendLike(
                            memberId,
                            success = {
                                requestSuccess(true)
                                setLikeState(actionButton, true)
                            },
                            failure = {
                                requestSuccess(false)
                            },
                            networkFail = {
                                requestSuccess(false)
                            }
                        )
                    } else {
                        CommonRepo.requestDislike(
                            memberId,
                            success = {
                                requestSuccess(true)
                                setLikeState(actionButton, false)
                            },
                            failure = {
                                requestSuccess(false)
                            },
                            networkFail = {
                                requestSuccess(false)
                            }
                        )
                    }
                } catch (e: Exception) {

                } finally {
                    // 항상 실행되는 블록 (필요한 경우)
                }

            }
        }


    }
}
