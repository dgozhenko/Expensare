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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.App
import com.example.expensare.R
import com.example.expensare.data.models.ListItem
import com.example.expensare.databinding.FragmentListBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.dashboard.DashboardFragmentDirections
import com.example.expensare.ui.dashboard.DashboardViewModel
import com.example.expensare.util.SwipeToDeleteCallback
import javax.inject.Inject

class ListFragment: BaseFragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val listViewModel by viewModels<ListViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
    }

    override fun onResume() {
        super.onResume()
        bindGroceryListRecyclerView()
    }

    private fun bindGroceryListRecyclerView() {
        val progressBar = binding.progressCircular
        progressBar.visibility = View.VISIBLE
        val checkedArray = arrayListOf<ListItem>()
        val adapter = ListAdapter(OnClickListener {listItem, isChecked ->
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
        listViewModel.groceryList.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.noListText.visibility = View.GONE
                adapter.getListItems(it)
                binding.progressCircular.visibility = View.GONE
            } else {
                binding.noListText.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
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