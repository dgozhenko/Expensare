package com.example.presentation.ui.mydebts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.lent.LentDebtsFragment
import com.example.presentation.ui.mydebts.owe.OweDebtsFragment
import com.google.android.material.tabs.TabLayout
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsBinding

class MyDebtsFragment : BaseFragment() {
  private var _binding: FragmentMyDebtsBinding? = null
  private val binding
    get() = _binding!!

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentMyDebtsBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    bindButtons()
    val tabLayout = binding.myDebtsTabLayout
    tabLayout.addTab(tabLayout.newTab().setText("Lent"))
    tabLayout.addTab(tabLayout.newTab().setText("Owe"))
    tabLayout.tabGravity = TabLayout.GRAVITY_FILL

    val adapter =
        MyDebtsFragmentViewPagerAdapter(
            childFragmentManager, tabLayout.tabCount, FragmentPagerAdapter.POSITION_UNCHANGED)

    adapter.addFragment(LentDebtsFragment())
    adapter.addFragment(OweDebtsFragment())
    binding.debtsViewPager.adapter = adapter
    tabLayout.getTabAt(1)?.select()
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

  private fun bindButtons() {
    binding.listButton.setBackgroundResource(R.drawable.rounded_button_left)
    binding.historyButton.setBackgroundResource(R.drawable.square_button)
    binding.debtButton.setBackgroundResource(R.drawable.rounded_button_right_chosen)

    binding.listButton.setOnClickListener {
      findNavController().navigate(MyDebtsFragmentDirections.actionMyDebtsFragmentToListFragment())
    }

    binding.historyButton.setOnClickListener {
      findNavController()
        .navigate(MyDebtsFragmentDirections.actionMyDebtsFragmentToDashboardFragment())
    }


    binding.addDebt.setOnClickListener {
      findNavController().navigate(MyDebtsFragmentDirections.actionMyDebtsFragmentToCreateDebtFragment())
    }
  }
}
