package com.example.expensare.ui.dashboard.finish_shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.databinding.FragmentDebtFinilizingBinding
import com.example.expensare.ui.base.BaseFragment

class FinishShopDebtDividerFragment: BaseFragment() {
    private var _binding: FragmentDebtFinilizingBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDebtFinilizingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmButton.setOnClickListener {
            findNavController().navigate(FinishShopDebtDividerFragmentDirections.actionFinishShopDebtDividerFragmentToDashboardFragment())
        }
    }

}