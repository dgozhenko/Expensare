package com.example.presentation.ui.dashboard.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.databinding.FragmentAddListItemScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddToListFragment: BaseFragment() {
    private var _binding: FragmentAddListItemScreenBinding? = null
    private val binding get() = _binding!!

    private val addToListViewModel: AddToListViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAddListItemScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        getUsers()
        addToListButtonClicked()
    }

    private fun getUsers() {
        addToListViewModel.group.observe(viewLifecycleOwner, {
            addToListViewModel.getUsersFromGroup(it)
        })
    }

    private fun addToListButtonClicked() {
        binding.addToListButton.setOnClickListener {
            val progressBar = binding.progressCircular
            progressBar.visibility = View.VISIBLE
            val name = binding.itemNameEditText.text.toString()
            var quantity = 0
            if (binding.quantityEditText.text.toString().isNotEmpty()) {
                quantity = binding.quantityEditText.text.toString().toInt()
            }
            val shop = "Walmart"
            val category = "Vegetables"
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Enter grocery item name, please",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progressBar.visibility = View.GONE
                }
                else -> {
                    addToListViewModel.user.observe(viewLifecycleOwner, {
                        addToListViewModel.createListItem(category, name, it, quantity, shop)
                    })
                    addToListViewModel.addToListResult.observe(viewLifecycleOwner, { result ->
                        when(result) {
                            AddToListResult.Success -> {
                                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                                findNavController().navigateUp()
                            }
                           is AddToListResult.Error -> {
                                Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                               progressBar.visibility = View.GONE
                            }
                        }
                    })
                }
            }
        }
    }

}