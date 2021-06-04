package com.example.expensare.ui.manage_group.debts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.R
import com.example.expensare.databinding.FragmentGroupDebtBinding
import com.example.expensare.ui.base.BaseFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlin.random.Random

class GroupDebtFragment: BaseFragment() {
    private var _binding: FragmentGroupDebtBinding? = null
    private val binding get() = _binding!!

    private var debtToMe = true

    private val groupDebtViewModel: GroupDebtViewModel by lazy { ViewModelProvider(this).get(GroupDebtViewModel::class.java) }

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
        debtToMe = true
        binding.pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (debtToMe) {
                    getDebts(false)
                    debtToMe = false
                    binding.oweStatusText.setTextColor(resources.getColor(R.color.red, requireActivity().theme))
                    binding.oweStatusText.text = getString(R.string.they_need_to_return_debt)
                } else {
                    getDebts(true)
                    debtToMe = true
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
            groupDebtViewModel.getUsersFromGroup(it)
        })
    }

    private fun getDebts(debtToMe: Boolean) {
        groupDebtViewModel.users.observe(viewLifecycleOwner, {
            groupDebtViewModel.getDebts(it, debtToMe)
        })
    }

    private fun bindRecyclerView() {
        val adapter = GroupDebtAdapter(debtToMe)
        binding.groupDebtRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupDebtRecyclerView.adapter = adapter
        groupDebtViewModel.userDebt.observe(viewLifecycleOwner, {
            adapter.getDebts(it)
            val pieChart = binding.pieChart
            val entries = arrayListOf<PieEntry>()
            val colors = arrayListOf<Int>()
            val rnd = Random
            it.forEach { debt ->
                colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
                entries.add(PieEntry(debt.fullAmount.toFloat(), debt.toUser.username))
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
        })
    }
}