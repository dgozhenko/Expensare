package com.example.expensare.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentDebtRequestsBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.requests.pending.PendingRequestsFragment
import com.example.expensare.ui.requests.requested.RequestedRequestsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class RequestsViewPagerFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentDebtRequestsBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDebtRequestsBinding.inflate(inflater)
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
        val tabLayout = binding.requestsTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Requested"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter =
            RequestsViewPagerAdapter(
                childFragmentManager, tabLayout.tabCount, FragmentPagerAdapter.POSITION_UNCHANGED)

        adapter.addFragment(RequestedRequestsFragment())
        adapter.addFragment(PendingRequestsFragment())

        binding.requestsViewPager.adapter = adapter
        binding.requestsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.requestsViewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        when(item.itemId) {
            R.id.dashboard -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(RequestsViewPagerFragmentDirections.actionRequestsViewPagerFragmentToDashboardFragment())
            }
            R.id.grocery_items -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(RequestsViewPagerFragmentDirections.actionRequestsViewPagerFragmentToSavedStoreFragment())
            }
            R.id.group_management -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(RequestsViewPagerFragmentDirections.actionRequestsViewPagerFragmentToGroupManagementFragment())
            }
            R.id.change_group -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(RequestsViewPagerFragmentDirections.actionRequestsViewPagerFragmentToChooseGroupFragment())
            }
            R.id.debt_request -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.settings -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(RequestsViewPagerFragmentDirections.actionRequestsViewPagerFragmentToSettingsFragment())
            }
            else -> false
        }
        return true
    }
}