package com.example.presentation.ui.group

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentRoomChooseBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseGroupFragment: BaseFragment() {
    private var _binding: FragmentRoomChooseBinding? = null
    private val binding get() = _binding!!

    private val chooseGroupViewModel: ChooseGroupViewModel by viewModels()

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
        val adapter = ChooseGroupAdapter(OnClickListener {
            chooseGroupViewModel.saveGroupID(it.groupID)
            findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToDashboardFragment())
        })

        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupRecyclerView.adapter = adapter
        chooseGroupViewModel.user.observe(viewLifecycleOwner, {
            chooseGroupViewModel.listenForGroups(it)
        })
        chooseGroupViewModel.groups.observe(viewLifecycleOwner, {
            adapter.getGroups(it)
        })
    }

}