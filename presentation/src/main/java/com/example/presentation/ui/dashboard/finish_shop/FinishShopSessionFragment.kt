package com.example.presentation.ui.dashboard.finish_shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentFinishShopSessionBinding

class FinishShopSessionFragment: BaseFragment() {
    private var _binding: FragmentFinishShopSessionBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentFinishShopSessionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.inflateMenu(R.menu.add_additional_item_menu)
        binding.absToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_additional_items -> {
                    findNavController().navigate(FinishShopSessionFragmentDirections.actionFinishShopSessionFragmentToAddAdditionalItemFragment())
                    true
                } else -> false
            }
        }
        binding.confirmButton.setOnClickListener {
            findNavController().navigate(FinishShopSessionFragmentDirections.actionFinishShopSessionFragmentToFinishShopDebtDividerFragment())
        }
    }
}