package com.example.presentation.ui.grocery_items.store

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
import com.inner_circles_apps.myapplication.databinding.FragmentGroceryItemStoresBinding

class SavedStoreFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentGroceryItemStoresBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroceryItemStoresBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.test_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar.inflateMenu(R.menu.add_additional_item_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_additional_items -> {
                    findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToAddStoreFragment())
                    true
                } else -> false
            }
        }
        binding.next.setOnClickListener {
            findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToCategoryFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)
        when(item.itemId) {
            R.id.dashboard -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToDashboardFragment())
            }
            R.id.grocery_items -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.group_management -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToGroupManagementFragment())
            }
            R.id.change_group -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToChooseGroupFragment())
            }
            R.id.debt_request -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToRequestsViewPagerFragment())
            }
            R.id.settings -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(SavedStoreFragmentDirections.actionSavedStoreFragmentToSettingsFragment())
            }
            else -> false
        }
        return true
    }
}