package com.example.presentation.ui.myhistory.owe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.myhistory.MyHistoryViewModel
import com.example.presentation.ui.myhistory.lent.LentHistoryRecyclerViewAdapter
import com.inner_circles_apps.myapplication.databinding.FragmentDebtsFromMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OweHistoryFragment : BaseFragment() {
    private var _binding: FragmentDebtsFromMeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: OweHistoryRecyclerViewAdapter? = null
    private val adapter get() = _adapter!!

    private val myHistoryViewModel : MyHistoryViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDebtsFromMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        myHistoryViewModel.oweHistory
        myHistoryViewModel.oweHistory.observe(
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
        _adapter = OweHistoryRecyclerViewAdapter()
        binding.debtsFromMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsFromMeRecyclerView.adapter = adapter
        myHistoryViewModel.oweHistory.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    binding.noOweManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it)
                }
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noOweManualDebtsText.text = it.message
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