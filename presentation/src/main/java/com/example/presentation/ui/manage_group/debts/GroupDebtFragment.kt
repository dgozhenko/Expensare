package com.example.presentation.ui.manage_group.debts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentGroupDebtBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class GroupDebtFragment: BaseFragment() {
    private var _binding: FragmentGroupDebtBinding? = null
    private val binding get() = _binding!!

    private var isLent = true

    private val groupDebtViewModel: GroupDebtViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentGroupDebtBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.absToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        getUsersFromGroup()
        getDebts(true)
        bindRecyclerView()
        isLent = true
        binding.pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (isLent) {
                    getDebts(false)
                    isLent = false
                    binding.oweStatusText.setTextColor(resources.getColor(R.color.red, requireActivity().theme))
                    binding.oweStatusText.text = getString(R.string.they_need_to_return_debt)
                } else {
                    getDebts(true)
                    isLent = true
                    binding.oweStatusText.setTextColor(resources.getColor(R.color.dark_green, requireActivity().theme))
                    binding.oweStatusText.text = getString(R.string.they_are_waiting_for_debt_return)
                }
                bindRecyclerView()
            }
            override fun onNothingSelected() {

            }
        })
    }

    private fun getUsersFromGroup() {
        groupDebtViewModel.group.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    // Progress
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Users here", Toast.LENGTH_LONG).show()
                    groupDebtViewModel.getUsersFromGroup(it.data!!)
                }
            }
        })
    }

    private fun getDebts(isLent: Boolean) {
        groupDebtViewModel.users.observe(viewLifecycleOwner, {
            groupDebtViewModel.getDebts(it, isLent)
        })
    }

    private fun bindRecyclerView() {
        val adapter = GroupDebtAdapter(isLent, OnClickListener { userDebt, recyclerView ->
            groupDebtViewModel.getDetailedDebts(userDebt.lentUser, isLent)
            val detailAdapter = DetailedGroupDebtAdapter(isLent)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = detailAdapter
            groupDebtViewModel.detailedGroupDebt.observe(viewLifecycleOwner, {
                when(it.status) {
                    Status.SUCCESS -> {
                        detailAdapter.getDebts(it.data!!)
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }

            })
        })

        binding.groupDebtRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupDebtRecyclerView.adapter = adapter
        groupDebtViewModel.groupDebt.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    adapter.getDebts(it.data!!)
                    val pieChart = binding.pieChart
                    val entries = arrayListOf<PieEntry>()
                    val colors = arrayListOf<Int>()
                    val rnd = Random
                    it.data!!.forEach { debt ->
                        colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
                        entries.add(PieEntry(debt.lentedAmount.toFloat(), debt.lentUser.username))
                    }
                    val dataSet = PieDataSet(entries, "users")
                    dataSet.colors = colors
                    pieChart.data = PieData(dataSet)
                    pieChart.highlightValue(null)
                    pieChart.invalidate()
                    val description = Description()
                    description.text = "Debt Chart"
                    pieChart.description = description
                    pieChart.spin( 500,0.0f,360f, Easing.EaseInOutQuad)
                }
            }
        })
    }
}