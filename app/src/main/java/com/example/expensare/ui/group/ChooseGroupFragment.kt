package com.example.expensare.ui.group

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.App
import com.example.expensare.R
import com.example.expensare.databinding.FragmentRoomChooseBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.dashboard.DashboardViewModel
import javax.inject.Inject

class ChooseGroupFragment: BaseFragment() {
    private var _binding: FragmentRoomChooseBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val chooseGroupViewModel by viewModels<ChooseGroupViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

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
                    findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToCreateGroupFragment())
                    true
                } else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bindRecyclerView()
    }

    private fun bindRecyclerView() {
        val adapter = ChooseGroupAdapter(OnClickListener {
            chooseGroupViewModel.saveGroupID(it.groupID)
            findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToDashboardFragment())
        })

        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupRecyclerView.adapter = adapter
        chooseGroupViewModel.listenForGroups()
        chooseGroupViewModel.groups.observe(viewLifecycleOwner, {
            adapter.getGroups(it)
        })
    }

}