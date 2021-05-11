package com.example.expensare.ui.dashboard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentDashboardBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class DashboardFragment: BaseFragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        binding.listButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left_chosen)
            binding.historyButton.setBackgroundResource(R.drawable.square_button)
            binding.historyRecyclerView.visibility = View.GONE
            binding.noHistoryText.visibility = View.GONE
            binding.noListText.visibility = View.VISIBLE
            binding.addNewItem.visibility = View.VISIBLE
            binding.addExpensesButton.visibility = View.GONE
        }

        binding.historyButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left)
            binding.historyButton.setBackgroundResource(R.drawable.square_button_chosen)
            binding.noHistoryText.visibility = View.VISIBLE
            binding.noListText.visibility = View.GONE
            binding.addNewItem.visibility = View.GONE
            binding.addExpensesButton.visibility = View.VISIBLE
        }

        binding.addExpensesButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddExpensesFragment())
        }

        binding.debtButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToMyDebtsFragment())
        }
    }
}