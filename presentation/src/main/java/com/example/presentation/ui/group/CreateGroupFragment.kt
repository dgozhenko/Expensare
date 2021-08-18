package com.example.presentation.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.util.Extensions.hideKeyboard
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentCreateGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroupFragment : BaseFragment() {
  private var _binding: FragmentCreateGroupBinding? = null
  private val binding
    get() = _binding!!

  private val createGroupViewModel: CreateGroupViewModel by viewModels()

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentCreateGroupBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    createGroupButtonClicked()
  }

  private fun createGroupButtonClicked() {
    val progress = binding.progressBar
    progress.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
    progress.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))
    binding.createGroupButton.setOnClickListener {
      it.hideKeyboard()
      progress.visibility = View.VISIBLE
      val groupNameEditText = binding.groupNameEditText.text.toString()
      when {
        groupNameEditText.isEmpty() -> {
          Toast.makeText(requireContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show()
          progress.visibility = View.GONE
        }
        else -> {
          createGroupViewModel.user.observe(
            viewLifecycleOwner,
            { userResponse ->
              when (userResponse.status) {
                Status.SUCCESS -> {
                  binding.progressBar.visibility = View.GONE
                  createGroupViewModel.createGroup(groupNameEditText, "Home", userResponse.data!!)
                }
                Status.ERROR -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), userResponse.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                  binding.progressBar.visibility = View.VISIBLE
                }
              }
            }
          )
          createGroupViewModel.createGroupResult.observe(
            viewLifecycleOwner,
            { result ->
              when (result) {
                is CreateGroupResult.Error -> {
                  Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT)
                    .show()
                  progress.visibility = View.GONE
                }
                CreateGroupResult.Success -> {
                  progress.visibility = View.GONE
                  findNavController().navigateUp()
                }
              }
            }
          )
        }
      }
    }
  }
}
