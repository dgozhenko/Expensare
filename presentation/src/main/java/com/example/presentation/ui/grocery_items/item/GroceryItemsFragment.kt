package com.example.presentation.ui.grocery_items.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentGroceryItemsBinding

class GroceryItemsFragment: BaseFragment() {
    private var _binding: FragmentGroceryItemsBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroceryItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        toolbar.inflateMenu(R.menu.add_cart_menu_item)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_cart_item -> {
                    findNavController().navigate(GroceryItemsFragmentDirections.actionGroceryItemsFragmentToAddGroceryItemFragment())
                    true
                } else -> false
            }
        }
    }
}