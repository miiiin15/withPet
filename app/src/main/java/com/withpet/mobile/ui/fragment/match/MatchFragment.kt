package com.example.yourapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.withpet.mobile.R

class MatchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 매칭 프래그먼트 레이아웃 인플레이트
        return inflater.inflate(R.layout.fragment_match, container, false)
    }
}
