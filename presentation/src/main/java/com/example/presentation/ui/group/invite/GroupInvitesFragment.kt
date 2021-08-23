package com.example.presentation.ui.group.invite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.User
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.databinding.FragmentGroupInvitesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupInvitesFragment: BaseFragment() {
    private var _binding: FragmentGroupInvitesBinding? = null
    private val binding get() = _binding!!

    private var _user: User? = null
    private val user get() = _user!!

    private var _adapter: GroupInvitesAdapter? = null
    private val adapter get() = _adapter!!

    private val invites: GroupInvitesFragmentArgs by navArgs()
    private val groupInvitesViewModel : GroupInvitesViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroupInvitesBinding.inflate(inflater)
        getUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        bindRecyclerView()
    }

    private fun setupToolbar() {
        binding.absToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindRecyclerView() {
         _adapter = GroupInvitesAdapter(OnClickListener{group, button ->
            if (button) {
                groupInvitesViewModel.addUserToGroup(group = group, user = user)
                groupInvitesViewModel.addUserResult.observe(viewLifecycleOwner, {
                    when(it.status) {
                        Status.SUCCESS -> {
                            groupInvitesViewModel.createUserInGroup(it.data!!)
                        }
                        Status.LOADING -> {
                            // progress
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
                groupInvitesViewModel.createUserResult.observe(viewLifecycleOwner, {
                    when(it.status) {
                        Status.SUCCESS -> {
                            findNavController().navigateUp()
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {

                        }
                    }
                })
            } else {
                groupInvitesViewModel.declineInvite(user, group)
                groupInvitesViewModel.declineInviteResult.observe(viewLifecycleOwner, {
                    when(it.status) {
                        Status.LOADING -> {

                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.SUCCESS -> {
                            findNavController().navigateUp()
                        }
                    }
                })
            }
        })
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupRecyclerView.adapter = adapter
        adapter.getInvites(invites.groupInvites)
    }

    private fun getUser() {
        groupInvitesViewModel.user.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.SUCCESS -> {
                    _user = it.data!!
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                Status.LOADING -> {

                }
            }
        })
    }

}