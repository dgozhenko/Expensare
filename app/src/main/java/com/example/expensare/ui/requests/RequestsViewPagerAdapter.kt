package com.example.expensare.ui.requests

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RequestsViewPagerAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    behavior: Int
): FragmentPagerAdapter(fragmentManager, behavior) {

    private val fragmentArray = arrayListOf<Fragment>()

    override fun getCount(): Int {
        return totalTabs
    }

    fun addFragment(fragment: Fragment) {
        fragmentArray.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArray[position]
    }

}
