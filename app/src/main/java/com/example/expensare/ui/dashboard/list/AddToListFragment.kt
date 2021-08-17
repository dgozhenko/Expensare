package com.example.expensare.ui.dashboard.list

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
import com.example.expensare.databinding.FragmentAddListItemScreenBinding
import com.example.expensare.ui.addexpense.AddExpenseViewModel
import com.example.expensare.ui.base.BaseFragment
import javax.inject.Inject

class AddToListFragment: BaseFragment() {
    private var _binding: FragmentAddListItemScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val addToListViewModel by viewModels<AddToListViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

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