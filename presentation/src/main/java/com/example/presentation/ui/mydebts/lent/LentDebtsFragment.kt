package com.example.presentation.ui.mydebts.lent

import android.app.DownloadManager
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Request
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsViewModel
import com.example.presentation.ui.requests.RequestsViewModel
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsToMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LentDebtsFragment : BaseFragment() {
    private var _binding: FragmentMyDebtsToMeBinding? = null
    private val binding get() = _binding!!
    private var _adapter: LentRecyclerViewAdapter? = null
    private val adapter get() = _adapter!!


    private val myDebtsViewModel: MyDebtsViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentMyDebtsToMeBinding.inflate(inflater)
        bindDebtsRecyclerView()
        return binding.root
    }


    private fun bindDebtsRecyclerView() {
        _adapter = LentRecyclerViewAdapter(LentRecyclerViewAdapter.OnClickListener { manualDebt ->
            myDebtsViewModel.deleteDebt(manualDebt)
            myDebtsViewModel.deleteDebtLiveData.observe(viewLifecycleOwner, {
                when(it.status) {
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Debt was deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        })
        binding.debtsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsToMeRecyclerView.adapter = adapter
        myDebtsViewModel.lentDebts.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS ->  {
                    binding.noLentManualDebtsText.visibility = View.GONE
                    adapter.getDebts(it)
                }
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noLentManualDebtsText.text = it.message
                }
            }
        })
        myDebtsViewModel.refreshedLentDebts.observe(viewLifecycleOwner, {
            when(it.status) {
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
        })
    }
}