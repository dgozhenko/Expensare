package com.example.presentation.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.GroupInvite
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentRoomChooseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGroupFragment : BaseFragment() {
  private var _binding: FragmentRoomChooseBinding? = null
  private val binding
    get() = _binding!!

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
          findNavController()
            .navigate(
              ChooseGroupFragmentDirections.actionChooseGroupFragmentToCreateGroupFragment()
            )
          true
        }
        else -> false
      }
    }
    listenForObservers()
  }

  override fun onResume() {
    super.onResume()
    bindRecyclerView()
  }

  private fun bindRecyclerView() {
    val adapter =
      ChooseGroupAdapter(
        OnClickListener {
          chooseGroupViewModel.saveGroupID(it.groupID)
          findNavController()
            .navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToDashboardFragment())
        }
      )

    binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    binding.groupRecyclerView.adapter = adapter
    chooseGroupViewModel.user.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          Status.SUCCESS -> {
            chooseGroupViewModel.listenForGroups(it.data!!)
            chooseGroupViewModel.getAllInvites(it.data!!)
          }
          Status.ERROR -> {
              Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
          }
            Status.LOADING -> {
                // TODO: 18.08.2021 add progressBar
            }
        }
      }
    )
    chooseGroupViewModel.groupIds.observe(viewLifecycleOwner, {
      when(it.status) {
        Status.LOADING -> {
          // TODO: 18.08.2021 add progressBar
        }
        Status.ERROR -> {
          Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
        Status.SUCCESS -> {
          chooseGroupViewModel.getAllGroups(it.data!!)
        }
      }
    })

    chooseGroupViewModel.groups.observe(viewLifecycleOwner, {
    when (it.status) {
      Status.SUCCESS -> {
        adapter.getGroups(it.data!!)
      }
      Status.ERROR -> {
        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
      }
      Status.LOADING -> {
        // TODO: 18.08.2021 add progressBar
      }
    }

    })
  }

  private fun listenForObservers() {
    chooseGroupViewModel.invites.observe(viewLifecycleOwner, { result ->
      when(result.status) {
        Status.LOADING -> {

        }
        Status.ERROR -> {
          Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
          binding.invitesCount.text = "0"
        }
        Status.SUCCESS -> {
          if (result.data != null) {
            binding.invitesCount.text = result.data!!.size.toString()
          } else {
            binding.invitesCount.text = "0"
          }
          binding.inviteDisplay.setOnClickListener {
            findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToGroupInvitesFragment(result.data!!))
          }
        }
      }
    })
  }
}
