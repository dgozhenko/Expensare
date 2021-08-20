package com.example.presentation.ui.dashboard.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GroupList
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.util.SwipeToDeleteCallback
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment: BaseFragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ListAdapter? = null
    private val adapter get() = _adapter!!

    private val listViewModel: ListViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentListBinding.inflate(inflater)
        bindGroceryListRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
    }

    override fun onResume() {
        super.onResume()
        listViewModel.refreshGroceryList()
        listViewModel.refreshedGroceryGroupList.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.noListText.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                    adapter.getListItems(it.data!!)
                }
                Status.ERROR -> {
                    binding.noListText.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun bindGroceryListRecyclerView() {
        val progressBar = binding.progressCircular
        progressBar.visibility = View.VISIBLE
        val checkedArray = arrayListOf<GroupList>()
         _adapter = ListAdapter(OnClickListener {listItem, isChecked ->
            if (isChecked) {
                checkedArray.add(listItem)
            } else {
                checkedArray.remove(listItem)
            }
            Toast.makeText(requireContext(), checkedArray.size.toString(), Toast.LENGTH_SHORT).show()
        })
        binding.listRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listRecyclerView.adapter = adapter
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.listRecyclerView.addItemDecoration(divider)
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.removeAt(viewHolder.adapterPosition)
                listViewModel.deleteGroceryItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerView)
        listViewModel.groceryGroupList.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.LOADING -> {
                    binding.noListText.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.noListText.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                    adapter.getListItems(it.data!!)
                }
                Status.ERROR -> {
                    binding.noListText.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun bindButtons() {
        binding.listButton.setBackgroundResource(R.drawable.rounded_button_left_chosen)
        binding.historyButton.setBackgroundResource(R.drawable.square_button)
        binding.debtButton.setBackgroundResource(R.drawable.rounded_button_right)

        binding.historyButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToDashboardFragment())
        }

        binding.debtButton.setOnClickListener {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToMyDebtsFragment())
        }

        binding.finishButton.setOnClickListener {
            findNavController()
                .navigate(
                    ListFragmentDirections.actionListFragmentToFinishShopSessionFragment()
                )
        }

        binding.addNewListItem.setOnClickListener {
            findNavController()
                .navigate(
                    ListFragmentDirections.actionListFragmentToAddToListFragment()
                )
        }
    }

}