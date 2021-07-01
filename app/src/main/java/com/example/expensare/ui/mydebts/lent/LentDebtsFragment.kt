package com.example.expensare.ui.mydebts.lent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.databinding.FragmentMyDebtsToMeBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.mydebts.MyDebtsViewModel

class LentDebtsFragment: BaseFragment() {
    private var _binding: FragmentMyDebtsToMeBinding? = null
    private val binding get() = _binding!!


    private val myDebtsViewModel: MyDebtsViewModel by lazy {
        ViewModelProvider(this).get(MyDebtsViewModel::class.java)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyDebtsToMeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLentUserDebts()
        bindDebtsRecyclerView()
    }

    private fun getLentUserDebts() {
        myDebtsViewModel.user.observe(viewLifecycleOwner, {
            myDebtsViewModel.getLentDebts(it)
        })
    }

    private fun bindDebtsRecyclerView() {
        val adapter = LentRecyclerViewAdapter()
        binding.debtsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsToMeRecyclerView.adapter = adapter
        myDebtsViewModel.lentDebts.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.getDebts(it)
            }
        })
    }
}