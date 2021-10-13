package com.example.presentation.ui.myhistory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyHistoryFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    behavior: Int
) : FragmentPagerAdapter(fragmentManager, behavior) {

    private val fragmentArrayList = arrayListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentArrayList.add(fragment)
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }
}