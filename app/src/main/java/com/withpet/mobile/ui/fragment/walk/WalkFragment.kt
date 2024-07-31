package com.withpet.mobile.ui.fragment.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.withpet.mobile.R

class WalkFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 산책 프래그먼트 레이아웃 인플레이트
        return inflater.inflate(R.layout.fragment_walk, container, false)
    }
}
