package com.example.presentation.ui.myhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsFragmentViewPagerAdapter
import com.example.presentation.ui.mydebts.lent.LentDebtsFragment
import com.example.presentation.ui.mydebts.owe.OweDebtsFragment
import com.example.presentation.ui.myhistory.lent.LentHistoryFragment
import com.example.presentation.ui.myhistory.owe.OweHistoryFragment
import com.google.android.material.tabs.TabLayout
import com.inner_circles_apps.myapplication.databinding.FragmentHistoryBinding
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsBinding

class MyHistoryFragment : BaseFragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbarTitle.setText("History")
        val tabLayout = binding.myDebtsTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Lent"))
        tabLayout.addTab(tabLayout.newTab().setText("Owe"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter =
            MyHistoryFragmentViewPagerAdapter(
                childFragmentManager, tabLayout.tabCount, FragmentPagerAdapter.POSITION_UNCHANGED)

        adapter.addFragment(LentHistoryFragment())
        adapter.addFragment(OweHistoryFragment())
        binding.debtsViewPager.adapter = adapter
        binding.debtsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.debtsViewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}