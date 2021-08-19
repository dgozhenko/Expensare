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
import com.example.domain.models.Status
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
            when(it.status) {
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    binding.progressCircular.visibility = View.GONE
                    addToListViewModel.getUsersFromGroup(it.data!!)
                }
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun addToListButtonClicked() {
        binding.addToListButton.setOnClickListener {
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
                }
                else -> {
                    addToListViewModel.user.observe(viewLifecycleOwner, {
                        when(it.status) {
                            Status.LOADING -> {
                                binding.progressCircular.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.progressCircular.visibility = View.GONE
                                addToListViewModel.createListItem(category, name, it.data!!, quantity, shop)
                            }
                            Status.ERROR -> {
                                binding.progressCircular.visibility = View.GONE
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            }
                        }

                    })

                    addToListViewModel.addToListResult.observe(viewLifecycleOwner, { result ->
                        when(result.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                                binding.progressCircular.visibility = View.GONE
                                findNavController().navigateUp()
                            }
                          Status.ERROR -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                              binding.progressCircular.visibility = View.GONE
                            }
                            Status.LOADING -> {
                                binding.progressCircular.visibility = View.VISIBLE
                            }
                        }
                    })
                }
            }
        }
    }

}