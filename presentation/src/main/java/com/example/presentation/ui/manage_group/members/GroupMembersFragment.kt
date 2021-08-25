package com.example.presentation.ui.manage_group.members

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Group
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.dialog.AddUserByEmailDialog
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentGroupMembersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupMembersFragment : BaseFragment() {
    private var _binding: FragmentGroupMembersBinding? = null
    private val binding
        get() = _binding!!

    private var _group: Group? = null
    private val group get() = _group

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
                    loadEmailDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadEmailDialog() {
        AddUserByEmailDialog().apply {
            listener = object : AddUserByEmailDialog.Listener {
                override fun onDialogButtonClicked(add: Boolean, email: String?) {
                    if (add && email!!.isNotBlank()) {
                        groupMembersViewModel.getUserByEmail(email)
                    } else {
                        dismiss()
                    }
                }
            }
        }.show(childFragmentManager, "EmailDialog")
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
        groupMembersViewModel.userByEmail.observe(viewLifecycleOwner, { user ->
                when (user.status) {
                    Status.LOADING -> {
                        // TODO: 20.08.2021 Progress
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), user.message, Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        groupMembersViewModel.sendInvite(user = user.data!!, group = group!!)
                    }
                }
            })

        groupMembersViewModel.group.observe(
            viewLifecycleOwner, {
                when(it.status) {
                    Status.SUCCESS -> {
                        _group = it.data!!
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
