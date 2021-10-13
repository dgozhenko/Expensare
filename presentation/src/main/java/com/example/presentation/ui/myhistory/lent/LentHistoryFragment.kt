package com.example.presentation.ui.myhistory.lent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsViewModel
import com.example.presentation.ui.mydebts.lent.LentRecyclerViewAdapter
import com.example.presentation.ui.myhistory.MyHistoryViewModel
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsToMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LentHistoryFragment : BaseFragment() {
    private var _binding: FragmentMyDebtsToMeBinding? = null
    private val binding get() = _binding!!
    private var _adapter: LentHistoryRecyclerViewAdapter? = null
    private val adapter get() = _adapter!!

    private val myHistoryViewModel: MyHistoryViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyDebtsToMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        myHistoryViewModel.lentHistory
        myHistoryViewModel.lentHistory.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.SUCCESS -> {
                        adapter.getDebts(it)
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        )
    }


    private fun bindDebtsRecyclerView() {
        _adapter = LentHistoryRecyclerViewAdapter()
         binding.debtsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsToMeRecyclerView.adapter = adapter
        myHistoryViewModel.lentHistory.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    binding.noLentManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it)
                }
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noLentManualDebtsText.text = it.message
                }
            }
        })
        /*myHistoryViewModel.refreshedLentDebts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noLentManualDebtsText.text = it.message
                }
                Status.SUCCESS -> {
                    binding.noLentManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it)
                }
                Status.LOADING -> {

                }
            }
        })*/
    }
}