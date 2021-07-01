package com.example.expensare.ui.mydebts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.databinding.FragmentMyDebtsToMeBinding
import com.example.expensare.ui.base.BaseFragment

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
        bindDebtsRecyclerView()
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