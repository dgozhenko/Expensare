package com.example.expensare.ui.addexpenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentAddExpensesBinding
import com.example.expensare.ui.base.BaseFragment

class AddExpensesFragment: BaseFragment() {
    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAddExpensesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.inflateMenu(R.menu.add_expenses_menu)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.absToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_expenses_button -> {
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    true
                } else -> false
            }
        }
    }
}