package com.example.expensare.ui.mydebts.create_debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.databinding.FragmentCreateManualDebtBinding
import com.example.expensare.ui.base.BaseFragment

class CreateDebtFragment: BaseFragment() {
    private var _binding: FragmentCreateManualDebtBinding? = null
    private val binding get() = _binding!!
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCreateManualDebtBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}