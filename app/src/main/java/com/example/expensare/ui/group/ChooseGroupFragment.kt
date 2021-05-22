package com.example.expensare.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentRoomChooseBinding
import com.example.expensare.ui.base.BaseFragment

class ChooseGroupFragment: BaseFragment() {
    private var _binding: FragmentRoomChooseBinding? = null
    private val binding get() = _binding!!
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRoomChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.inflateMenu(R.menu.add_additional_item_menu)
        binding.next.setOnClickListener {
            findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToDashboardFragment())
        }
        binding.absToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_additional_items -> {
                    findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToCreateGroupFragment())
                    true
                } else -> false
            }
        }
    }

}