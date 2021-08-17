package com.example.expensare.ui.group

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.App
import com.example.expensare.R
import com.example.expensare.databinding.FragmentCreateGroupBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.util.Extensions.hideKeyboard
import javax.inject.Inject

class CreateGroupFragment : BaseFragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createGroupViewModel by viewModels<CreateGroupViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

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
                    Toast.makeText(
                            requireContext(),
                            "Group name cannot be empty",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    progress.visibility = View.GONE
                }
                else -> {
                    createGroupViewModel.createGroup(groupNameEditText, "Home")
                    createGroupViewModel.createGroupResult.observe(viewLifecycleOwner, { result ->
                        when (result) {
                            is CreateGroupResult.Error -> {
                                Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                                progress.visibility = View.GONE
                            }
                            CreateGroupResult.Success -> {
                                progress.visibility = View.GONE
                                findNavController().navigateUp()
                            }
                        }
                    })
                }
            }
        }
    }
}
