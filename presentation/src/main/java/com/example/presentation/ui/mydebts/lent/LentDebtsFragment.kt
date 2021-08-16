package com.example.presentation.ui.mydebts.lent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsViewModel
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsToMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LentDebtsFragment: BaseFragment() {
    private var _binding: FragmentMyDebtsToMeBinding? = null
    private val binding get() = _binding!!
    private var _adapter: LentRecyclerViewAdapter? = null
    private val adapter get() = _adapter


    private val myDebtsViewModel: MyDebtsViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyDebtsToMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        myDebtsViewModel.refreshLentDebts()
            myDebtsViewModel.refreshedLentDebts.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter!!.getDebts(it)
                }
            })
    }

    private fun bindDebtsRecyclerView() {
         _adapter = LentRecyclerViewAdapter()
        binding.debtsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsToMeRecyclerView.adapter = adapter
        myDebtsViewModel.lentDebts.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter!!.getDebts(it)
            }
        })
        myDebtsViewModel.updatedLentDebts.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.debtsToMeRecyclerView.visibility = View.VISIBLE
                adapter!!.getDebts(it)
            } else {
                binding.debtsToMeRecyclerView.visibility = View.GONE
            }
        })
    }
}