package com.example.expensare.ui.manage_group.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.R
import com.example.expensare.databinding.FragmentGroupMembersBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class GroupMembersFragment: BaseFragment() {
    private var _binding: FragmentGroupMembersBinding? = null
    private val binding get() = _binding!!

    private val groupMembersViewModel: GroupMembersViewModel by lazy { ViewModelProvider(this).get(GroupMembersViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroupMembersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        toolbar.inflateMenu(R.menu.add_member_menu)
        getMembers()
        bindRecyclerView()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_member_menu_item -> {
                    groupMembersViewModel.getUserByEmail("zeratskilsotov@gmail.com")
                    groupMembersViewModel.userByEmail.observe(viewLifecycleOwner, { user->
                    groupMembersViewModel.addUserToGroup(user)
                    })
                    true
                } else -> false
            }
        }
    }

    private fun getMembers() {
        groupMembersViewModel.group.observe(viewLifecycleOwner, {
            groupMembersViewModel.getUsersFromGroup(it)
        })
    }

    private fun bindRecyclerView() {
        val adapter = GroupMembersAdapter()
        binding.groupMembersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupMembersRecyclerView.adapter = adapter
        groupMembersViewModel.user.observe(viewLifecycleOwner, {
            adapter.setUser(it)
        })
    }
}