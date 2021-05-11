package com.example.expensare.ui.mydebts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensare.databinding.FragmentMyDebtsToMeBinding
import com.example.expensare.ui.base.BaseFragment

class ToMeDebtsFragment: BaseFragment() {
    private var _binding: FragmentMyDebtsToMeBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyDebtsToMeBinding.inflate(inflater)
        return binding.root
    }
}