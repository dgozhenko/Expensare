package com.example.presentation.ui.manage_group.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Status
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentGroupMembersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupMembersFragment : BaseFragment() {
    private var _binding: FragmentGroupMembersBinding? = null
    private val binding
        get() = _binding!!

    private val groupMembersViewModel: GroupMembersViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroupMembersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        toolbar.inflateMenu(R.menu.add_member_menu)
        listenToObservers()
        bindRecyclerView()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_member_menu_item -> {
                    groupMembersViewModel.getUserByEmail("sashaprokipchuk@gmail.com")
                    true
                }
                else -> false
            }
        }
    }

    private fun bindRecyclerView() {
        val adapter = GroupMembersAdapter()
        binding.groupMembersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupMembersRecyclerView.adapter = adapter
        groupMembersViewModel.user.observe(viewLifecycleOwner, {
            adapter.setUser(it)
        })
    }
    private fun listenToObservers() {
        groupMembersViewModel.addUserToGroupResult.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    groupMembersViewModel.createUserInGroup(it.data!!)
                }
            }
        })

        groupMembersViewModel.userByEmail.observe(viewLifecycleOwner, { user ->
                when (user.status) {
                    Status.LOADING -> {
                        // TODO: 20.08.2021 Progress
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), user.message, Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        groupMembersViewModel.addUserToGroup(user.data!!)
                    }
                }
            })

        groupMembersViewModel.createUserInGroupResult.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), it.data!!, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        groupMembersViewModel.group.observe(
            viewLifecycleOwner, {
                when(it.status) {
                    Status.SUCCESS -> {
                        groupMembersViewModel.getUsersFromGroup(it.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }

            })
    }
}
