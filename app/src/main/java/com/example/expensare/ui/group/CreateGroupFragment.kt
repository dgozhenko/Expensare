package com.example.expensare.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.databinding.FragmentCreateGroupBinding
import com.example.expensare.ui.base.BaseFragment

class CreateGroupFragment: BaseFragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!

    val createGroupViewModel: CreateGroupViewModel by lazy { ViewModelProvider(this).get(CreateGroupViewModel::class.java) }

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
        binding.createGroupButton.setOnClickListener {
            var errorFree = true
            val groupNameEditText = binding.groupNameEditText.text.toString()
            when {
                groupNameEditText.isEmpty() -> {
                    Toast.makeText(requireContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show()
                } else -> {
                    createGroupViewModel.createGroup(groupNameEditText, "Home")
                    createGroupViewModel.error.observe(viewLifecycleOwner, {
                        if (it != null) {
                            errorFree = false
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            createGroupViewModel.errorDelivered()
                        }
                    })

                if (errorFree) {
                    createGroupViewModel.isComplete.observe(viewLifecycleOwner, {
                        if (it) {
                            findNavController().navigateUp()
                            createGroupViewModel.completed()
                        } else {
                            Toast.makeText(requireContext(), "Some error appear", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                }
            }
        }
    }
}