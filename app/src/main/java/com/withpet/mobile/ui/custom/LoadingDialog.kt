package com.withpet.mobile.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.withpet.mobile.R
import com.withpet.mobile.databinding.DialogLoadingBinding

class LoadingDialog : DialogFragment() {

    lateinit var binding: DialogLoadingBinding

    // isAdded 등의 플래그가 변경되기 전에 show를 호출할 수 있으므로
    // 별도의 플래그를 둔다.
    private var isLoading = false


    override fun getTheme() = R.style.AppDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_loading,
            container,
            false
        )

        binding.lifecycleOwner = this

        binding.pgb.let {
            Glide.with(this).load(R.raw.load).into(it)
        }

        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        setCancelable(false)
        // progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return super.onCreateDialog(savedInstanceState)
    }


    override fun show(manager: FragmentManager, tag: String?) {
        if (!isLoading) {
            isLoading = true

            // 홈화면으로 나갔을 때 show 하면 IllegalStateException 발생
            try {
                super.show(manager, tag)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

        }
    }

    override fun dismiss() {
        if (isLoading) {
            isLoading = false
            super.dismissAllowingStateLoss()
        }
    }

}