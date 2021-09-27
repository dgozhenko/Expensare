package com.example.presentation.ui.mydebts.lent

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Request
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.mydebts.MyDebtsViewModel
import com.example.presentation.ui.requests.RequestsViewModel
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentMyDebtsToMeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.lang.RuntimeException

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

    override fun onResume() {
        super.onResume()
        myDebtsViewModel.refreshLentDebts()
        myDebtsViewModel.refreshedLentDebts.observe(
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
        _adapter = LentRecyclerViewAdapter(LentRecyclerViewAdapter.OnClickListener { manualDebt ->
            myDebtsViewModel.deleteDebt(manualDebt)
            myDebtsViewModel.deleteDebtLiveData.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                    Status.SUCCESS -> {
                        findNavController().navigate(R.id.action_myDebtsFragment_to_myDebtsFragment)
                        Toast.makeText(
                            requireContext(),
                            "Debt was deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        })
        binding.debtsToMeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.debtsToMeRecyclerView.adapter = adapter
        myDebtsViewModel.lentDebts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    binding.noLentManualDebtsText.visibility = GONE
                    adapter.getDebts(it)
                }
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noLentManualDebtsText.text = it.message
                }
            }
        })
        myDebtsViewModel.refreshedLentDebts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.noLentManualDebtsText.text = it.message
                }
                Status.SUCCESS -> {
                    binding.noLentManualDebtsText.visibility = GONE
                    adapter.getDebts(it)
                }
                Status.LOADING -> {

                }
            }
        })
    }
}