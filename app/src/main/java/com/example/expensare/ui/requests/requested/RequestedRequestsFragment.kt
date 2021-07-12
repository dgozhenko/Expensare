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
import com.example.expensare.ui.requests.pending.PendingRecyclerViewAdapter

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
        getToMeUserRequests()
        bindDebtsRecyclerView()
    }

    private fun getToMeUserRequests() {
        requestsViewModel.user.observe(viewLifecycleOwner, {
            requestsViewModel.getPendingRequests()
        })
    }


    private fun bindDebtsRecyclerView() {
        val adapter = RequestedRecyclerViewAdapter()
        binding.requestsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.requestsToMeRecyclerView.adapter = adapter
        requestsViewModel.toMeRequests.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.getRequests(it)
            }
        })
    }
}