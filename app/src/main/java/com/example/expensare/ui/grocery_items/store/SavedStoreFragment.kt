package com.example.expensare.ui.grocery_items.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentGroceryItemStoresBinding
import com.example.expensare.ui.base.BaseFragment

class SavedStoreFragment: BaseFragment() {
    private var _binding: FragmentGroceryItemStoresBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroceryItemStoresBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
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
}