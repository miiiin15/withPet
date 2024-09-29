package com.withpet.mobile.ui.custom


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.withpet.mobile.data.model.Someone
import com.withpet.mobile.databinding.CustomViewSomeoneInfoBinding
import com.withpet.mobile.utils.Constants

class SomeoneInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CustomViewSomeoneInfoBinding? = null
    private val binding get() = _binding!!

    private var btnGreetClickListener: View.OnClickListener? = null
    private var btnLikeClickListener: View.OnClickListener? = null

    private var data: Someone? = null
    private var profileImageResId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomViewSomeoneInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconClose.setOnClickListener {
            dismiss()
        }

        binding.btnGreet.setOnClickListener {
            btnGreetClickListener?.onClick(it)
        }

        binding.btnLike.setOnClickListener {
            btnLikeClickListener?.onClick(it)
        }

        // 데이터 설정
        val fullImageUrl =
            Constants.IMAGE_URL + "media" + data?.profileImage?.replace("\\", "/")
        Glide.with(this).load(fullImageUrl).into(binding.imgProfile)
        binding.tvUserName.text = data?.nickName ?: "알수 없음"
        binding.tvUserGender.text =
            if (data?.sexType == "MALE") "남자" else if (data?.sexType == "FEMALE") "여자" else "알수 없음"
        binding.tvUserAge.text = (data?.age ?: 0).toString() + "세"
        binding.tvPetName.text = data?.petName ?: "알수 없음"
        binding.tvPetGender.text =
            if (data?.petSex == "남자") "남아" else if (data?.petSex == "여자") "여아" else "알수 없음"
        binding.tvPetDesc.text = data?.introduction ?: "소개글 없음"
    }

    fun setData(someone: Someone) {
        data = someone
    }

    fun setOnGreetClickListener(listener: View.OnClickListener) {
        btnGreetClickListener = listener
    }

    fun setOnLikeClickListener(listener: View.OnClickListener) {
        btnLikeClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as ViewGroup
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                isHideable = false
            }
        }
        return dialog
    }

    companion object {
        fun show(fragmentManager: FragmentManager): SomeoneInfoBottomSheet {
            val bottomSheet = SomeoneInfoBottomSheet()
            bottomSheet.show(fragmentManager, bottomSheet.tag)
            return bottomSheet
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
