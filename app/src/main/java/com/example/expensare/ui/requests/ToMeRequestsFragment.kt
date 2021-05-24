package com.example.expensare.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensare.databinding.FragmentMyRequestsBinding
import com.example.expensare.ui.base.BaseFragment

class ToMeRequestsFragment: BaseFragment() {
    private var _binding: FragmentMyRequestsBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyRequestsBinding.inflate(inflater)
        return binding.root
    }
}