package com.example.expensare.ui.mydebts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyDebtsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    behavior: Int
) : FragmentPagerAdapter(fragmentManager, behavior) {

    private val fragmentArrayList = arrayListOf<Fragment>()

    override fun getCount(): Int {
        return totalTabs
    }

    fun addFragment(fragment: Fragment){
        fragmentArrayList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }
}