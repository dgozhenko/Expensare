package com.example.presentation.ui.grocery_items.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.databinding.FragmentAddGroceryItemBinding

class AddGroceryItemFragment: BaseFragment() {
    private var _binding: FragmentAddGroceryItemBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAddGroceryItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}