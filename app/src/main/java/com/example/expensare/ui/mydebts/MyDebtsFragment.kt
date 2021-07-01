package com.example.expensare.ui.mydebts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentMyDebtsBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout

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
    binding.absToolbar.inflateMenu(R.menu.my_debts_menu)
    binding.absToolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.create_debt_menu_button -> {
          findNavController().navigate(MyDebtsFragmentDirections.actionMyDebtsFragmentToCreateDebtFragment())
          true
        } else -> false
      }
    }
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
