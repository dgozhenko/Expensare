package com.example.expensare.ui.requests.pending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.databinding.FragmentMyRequestsBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.requests.RequestsViewModel
import com.example.expensare.ui.requests.toMe.ToMeRecyclerViewAdapter

class PendingRequestsFragment: BaseFragment() {
    private var _binding: FragmentMyRequestsBinding? = null
    private val binding get() = _binding!!

    private val requestsViewModel: RequestsViewModel by lazy {
        ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyRequestsBinding.inflate(inflater)
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
        val adapter = ToMeRecyclerViewAdapter()
        binding.requestsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.requestsToMeRecyclerView.adapter = adapter
        requestsViewModel.toMeRequests.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.getRequests(it)
            }
        })
    }
}