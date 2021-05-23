package com.example.expensare.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentPagerAdapter
import com.example.expensare.R
import com.example.expensare.databinding.FragmentDebtRequestsBinding
import com.example.expensare.databinding.FragmentMyRequestsBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.mydebts.FromMeDebtsFragment
import com.example.expensare.ui.mydebts.MyDebtsFragmentViewPagerAdapter
import com.example.expensare.ui.mydebts.ToMeDebtsFragment
import com.google.android.material.tabs.TabLayout

class RequestsViewPagerFragment: BaseFragment() {
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
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        val tabLayout = binding.requestsTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Requests To Me "))
        tabLayout.addTab(tabLayout.newTab().setText("Pending Requests"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter =
            RequestsViewPagerAdapter(
                childFragmentManager, tabLayout.tabCount, FragmentPagerAdapter.POSITION_UNCHANGED)

        adapter.addFragment(ToMeRequestsFragment())
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
}