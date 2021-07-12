package com.example.expensare.ui.mydebts.owe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.databinding.FragmentDebtsFromMeBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.mydebts.MyDebtsViewModel
import com.example.expensare.ui.mydebts.lent.LentRecyclerViewAdapter

class OweDebtsFragment: BaseFragment() {
    private var _binding: FragmentDebtsFromMeBinding? = null
    private val binding get() = _binding!!

    private val myDebtsViewModel: MyDebtsViewModel by lazy {
        ViewModelProvider(this).get(MyDebtsViewModel::class.java)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDebtsFromMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindDebtsRecyclerView() {
        val adapter = OweRecyclerViewAdapter(OweRecyclerViewAdapter.OnClickListener { manualDebt ->
            myDebtsViewModel.createRequest(manualDebt)
        })
        binding.debtsFromMeRecyclerView .layoutManager = LinearLayoutManager(requireContext())
        binding.debtsFromMeRecyclerView.adapter = adapter
//        binding.debtsFromMeRecyclerView.itemAnimator = null
        myDebtsViewModel.oweDebts.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.getDebts(it)
            }
        })
        myDebtsViewModel.updatedOweDebts.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.debtsFromMeRecyclerView.visibility = View.VISIBLE
                adapter.getDebts(it)
            } else {
                binding.debtsFromMeRecyclerView.visibility = View.GONE
            }
        })
    }
}