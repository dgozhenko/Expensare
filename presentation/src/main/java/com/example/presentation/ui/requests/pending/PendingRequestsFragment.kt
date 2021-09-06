package com.example.presentation.ui.requests.pending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.requests.RequestsViewModel
import com.inner_circles_apps.myapplication.databinding.FragmentPendingRequestsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingRequestsFragment: BaseFragment() {
    private var _binding: FragmentPendingRequestsBinding? = null
    private val binding get() = _binding!!

    private val requestsViewModel: RequestsViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentPendingRequestsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPendingUserRequests()
        bindDebtsRecyclerView()
    }

    private fun getPendingUserRequests() {
        requestsViewModel.user.observe(viewLifecycleOwner, {
            requestsViewModel.getPendingRequests()
        })
    }


    private fun bindDebtsRecyclerView() {
        val adapter = PendingRecyclerViewAdapter()
        binding.pendingRequestsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.pendingRequestsRecyclerView.adapter = adapter
        requestsViewModel.pendingRequests.observe(viewLifecycleOwner, {
            if (it.data != null) {
                adapter.getRequests(it)
            }
        })
    }
}