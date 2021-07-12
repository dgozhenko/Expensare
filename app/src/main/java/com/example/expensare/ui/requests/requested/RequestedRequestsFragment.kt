package com.example.expensare.ui.requests.requested

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.databinding.FragmentRequestedRequestsBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.requests.RequestsViewModel

class RequestedRequestsFragment: BaseFragment() {
    private var _binding: FragmentRequestedRequestsBinding? = null
    private val binding get() = _binding!!

    private val requestsViewModel: RequestsViewModel by lazy {
        ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRequestedRequestsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRequestedRequests()
        bindDebtsRecyclerView()
    }

    private fun getRequestedRequests() {
        requestsViewModel.user.observe(viewLifecycleOwner, {
            requestsViewModel.getRequestedRequests()
        })
    }


    private fun bindDebtsRecyclerView() {
        val adapter = RequestedRecyclerViewAdapter()
        binding.requestedRequestsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.requestedRequestsRecyclerView.adapter = adapter
        requestsViewModel.requestedRequests.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.getRequests(it)
            }
        })
    }
}