package com.example.expensare.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensare.databinding.FragmentPendingRequestsBinding
import com.example.expensare.ui.base.BaseFragment

class PendingRequestsFragment: BaseFragment() {
    private var _binding: FragmentPendingRequestsBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentPendingRequestsBinding.inflate(inflater)
        return binding.root
    }
}