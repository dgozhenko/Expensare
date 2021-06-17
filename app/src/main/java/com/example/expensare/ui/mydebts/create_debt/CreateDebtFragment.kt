package com.example.expensare.ui.mydebts.create_debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.data.User
import com.example.expensare.databinding.FragmentCreateManualDebtBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.ui.dashboard.DashboardFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CreateDebtFragment: BaseFragment() {
    private var _binding: FragmentCreateManualDebtBinding? = null
    private val binding get() = _binding!!

    private val createDebtViewModel: CreateDebtViewModel by lazy {
        ViewModelProvider(this).get(CreateDebtViewModel::class.java)
    }
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCreateManualDebtBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        getUserInfo()
        getUsersFromGroup()

        binding.toUserName.setOnClickListener{
            var arraySize = 0
            val singleItemArrayList = arrayListOf<String>()
            createDebtViewModel.users.observe(viewLifecycleOwner, {
                it.forEach { user->
                    arraySize++
                }
            })
            var singleItems = Array<String>(arraySize){""}
            arraySize = 0
            createDebtViewModel.users.observe(viewLifecycleOwner, {
                it.forEach { user->
                    singleItems[arraySize] = user.username
                    arraySize++
                }
            })
            val checkedItem = 0
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Choose debtor")
                .setNeutralButton("Cancel") { dialog, which -> }
                .setPositiveButton("OK") { dialog, which -> }
                .setSingleChoiceItems(singleItems, 0) { dialog, which ->
                    binding.toUserName.setText(singleItems[which])
                }
                .show()
        }
        binding.loginButton.setOnClickListener{
            val debtFor = binding.debtForEditText.text.toString()
            val debtAmount = binding.debtAmountEditText.text.toString()
            when {
                debtFor.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Enter what the debt is for, please",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                debtAmount.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Enter debt amount, please",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    createDebtViewModel.createDebt(debtFor,debtAmount.toInt(), User("sdfv","df","sdvsdvcsd@gmail.com", "sdfvdsv"), User("sdfv","df","sdvsdvcsd@gmail.com", "sdfvdsv"))
                    Toast.makeText(
                        requireContext(),
                        "Debt was successfully created",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun getUsersFromGroup() {
        createDebtViewModel.group.observe(viewLifecycleOwner, {
            createDebtViewModel.getUsersFromGroup(it)
        })
    }

    private fun getUserInfo() {
        createDebtViewModel.user.observe(
            viewLifecycleOwner,
            {
                if (it == null) {
                    findNavController()
                        .navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
                        )
                    FirebaseAuth.getInstance().signOut()
                } else {
                    binding.fromUserName.setText(it.username)
                }
            }
        )
    }
}