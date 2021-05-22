package com.example.expensare.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.databinding.FragmentCreateGroupBinding
import com.example.expensare.ui.base.BaseFragment

class CreateGroupFragment: BaseFragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCreateGroupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.createGroupButton.setOnClickListener { findNavController().navigateUp() }
    }
}