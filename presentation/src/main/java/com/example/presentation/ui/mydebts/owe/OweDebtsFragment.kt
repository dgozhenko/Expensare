package com.example.presentation.ui.mydebts.owe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsViewModel
import com.inner_circles_apps.myapplication.databinding.FragmentDebtsFromMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OweDebtsFragment : BaseFragment() {
    private var _binding: FragmentDebtsFromMeBinding? = null
    private val binding get() = _binding!!
    private var _adapter: OweRecyclerViewAdapter? = null
    private val adapter get() = _adapter!!

    private val myDebtsViewModel: MyDebtsViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDebtsFromMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }

    private fun bindDebtsRecyclerView() {
        _adapter = OweRecyclerViewAdapter(OweRecyclerViewAdapter.OnClickListener { manualDebt ->
            myDebtsViewModel.createRequest(manualDebt)
        })
        binding.debtsFromMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsFromMeRecyclerView.adapter = adapter
//        binding.debtsFromMeRecyclerView.itemAnimator = null
        myDebtsViewModel.oweDebts.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noOweManualDebtsText.text = it.message
                }
                Status.SUCCESS -> {
                    binding.noOweManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it.data!!)
                }
                Status.LOADING -> {

                }
            }

        })
        myDebtsViewModel.refreshedOweDebts.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noOweManualDebtsText.text = it.message
                }
                Status.SUCCESS -> {
                    binding.noOweManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it.data!!)
                }
                Status.LOADING -> {

                }
            }
        })
    }
}