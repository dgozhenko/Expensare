package com.example.expensare.ui.manage_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentManageGroupBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView

class GroupManagementFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
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

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        when(item.itemId) {
            R.id.dashboard -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToDashboardFragment())
            }
            R.id.grocery_items -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToSavedStoreFragment())
            }
            R.id.group_management -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.change_group -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToChooseGroupFragment())
            }
            R.id.debt_request -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToRequestsViewPagerFragment())
            }
            R.id.settings -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(GroupManagementFragmentDirections.actionGroupManagementFragmentToSettingsFragment())
            }
        }
        return true
    }

}