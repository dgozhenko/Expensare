package com.example.expensare.ui.dashboard.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentListBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.dashboard.DashboardFragmentDirections

class ListFragment: BaseFragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
    }

    private fun bindButtons() {
        binding.listButton.setBackgroundResource(R.drawable.rounded_button_left_chosen)
        binding.historyButton.setBackgroundResource(R.drawable.square_button)
        binding.debtButton.setBackgroundResource(R.drawable.rounded_button_right)

        binding.historyButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToDashboardFragment())
        }

        binding.debtButton.setOnClickListener {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToMyDebtsFragment())
        }

        binding.finishButton.setOnClickListener {
            findNavController()
                .navigate(
                    ListFragmentDirections.actionListFragmentToFinishShopSessionFragment()
                )
        }

        binding.addNewListItem.setOnClickListener {
            findNavController()
                .navigate(
                    ListFragmentDirections.actionListFragmentToAddToListFragment()
                )
        }
    }

}