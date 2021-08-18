package com.example.presentation.ui.manage_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentManageGroupBinding

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
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.test_nav_view)
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
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)
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