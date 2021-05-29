package com.example.expensare.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.R
import com.example.expensare.databinding.FragmentRoomChooseBinding
import com.example.expensare.ui.base.BaseFragment

class ChooseGroupFragment: BaseFragment() {
    private var _binding: FragmentRoomChooseBinding? = null
    private val binding get() = _binding!!

    private val chooseGroupViewModel: ChooseGroupViewModel by lazy { ViewModelProvider(this).get(ChooseGroupViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRoomChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.inflateMenu(R.menu.add_additional_item_menu)
        binding.absToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_additional_items -> {
                    findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToCreateGroupFragment())
                    true
                } else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bindRecyclerView()
    }

    private fun bindRecyclerView() {
        val adapter = ChooseGroupAdapter()
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupRecyclerView.adapter = adapter
        chooseGroupViewModel.listenForGroups()
        chooseGroupViewModel.groups.observe(viewLifecycleOwner, {
            adapter.getGroups(it)
        })
    }

}