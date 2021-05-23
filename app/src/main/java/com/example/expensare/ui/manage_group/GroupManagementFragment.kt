package com.example.expensare.ui.manage_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentManageGroupBinding
import com.example.expensare.ui.base.BaseFragment

class GroupManagementFragment: BaseFragment() {
    private var _binding: FragmentManageGroupBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentManageGroupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToGroupSettingsFragment())
        }
        binding.membersButton.setOnClickListener {
            findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToGroupMembersFragment())
        }
        binding.debtButton.setOnClickListener {
            findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToGroupDebtFragment())
        }
    }

}