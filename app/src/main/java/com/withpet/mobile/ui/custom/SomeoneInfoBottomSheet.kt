package com.withpet.mobile.ui.custom


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.withpet.mobile.databinding.CustomViewSomeoneInfoBinding

class SomeoneInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CustomViewSomeoneInfoBinding? = null
    private val binding get() = _binding!!

    private var btnGreetClickListener: View.OnClickListener? = null
    private var btnLikeClickListener: View.OnClickListener? = null

    private var profileImageResId: Int = 0
    private var userName: String? = null
    private var userGender: String? = null
    private var userAge: String? = null
    private var petName: String? = null
    private var petDescription: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomViewSomeoneInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        binding.imgProfile.setImageResource(profileImageResId)
        binding.tvUserName.text = userName
        binding.tvUserGender.text = userGender
        binding.tvUserAge.text = userAge
//        binding.tvPetName.text = petName
//        binding.tvPetDesc.text = petDescription
    }

    // TODO : 메서드 합치기

    fun setProfileImage(resourceId: Int) {
        profileImageResId = resourceId
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun setUserGender(userGender: String) {
        this.userGender = if (userGender == "Male") "남자" else "여자"
    }

    fun setUserAge(userAge: Int) {
        this.userAge = "${userAge}세 "
    }

    fun setPetName(petName: String) {
        this.petName = petName
    }

    fun setPetDescription(petDescription: String) {
        this.petDescription = petDescription
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
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as ViewGroup
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
