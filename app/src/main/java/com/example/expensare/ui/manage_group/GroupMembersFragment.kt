package com.example.expensare.ui.manage_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentGroupMembersBinding
import com.example.expensare.ui.base.BaseFragment

class GroupMembersFragment: BaseFragment() {
    private var _binding: FragmentGroupMembersBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroupMembersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        toolbar.inflateMenu(R.menu.add_member_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_member_menu_item -> {
                    Toast.makeText(requireContext(), "Add member", Toast.LENGTH_SHORT).show()
                    true
                } else -> false
            }
        }
    }
}