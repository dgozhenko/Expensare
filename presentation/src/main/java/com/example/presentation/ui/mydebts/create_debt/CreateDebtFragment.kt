package com.example.presentation.ui.mydebts.create_debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.domain.database.entities.SecondUserEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.util.Status
import com.example.domain.models.User
import com.example.presentation.ui.auth.registration.RegistrationFragmentDirections
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.ui.dashboard.DashboardFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentCreateManualDebtBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDebtFragment : BaseFragment() {
    private var _binding: FragmentCreateManualDebtBinding? = null
    private val binding get() = _binding!!

    private var userFrom: User? = null
    private var userTo: User? = null

    private val createDebtViewModel: CreateDebtViewModel by viewModels()
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCreateManualDebtBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        getUserInfo()
        getUsersFromGroup()

        binding.toUserName.setOnClickListener {
            var arraySize = createDebtViewModel.users.size
            var singleItems = Array<String>(arraySize) { "" }
            arraySize = 0
            createDebtViewModel.users.forEach { user ->
                singleItems[arraySize] = user.username
                arraySize++
            }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Choose debtor")
                .setNegativeButton("Cancel") { dialog, which ->
                    binding.toUserAvatar.setImageResource(R.drawable.ic_launcher_foreground)
                    binding.toUserName.setText("user")
                }
                .setPositiveButton("OK") { dialog, which -> }
                .setSingleChoiceItems(singleItems, 0) { dialog, which ->
                    binding.toUserName.setText(singleItems[which])
                    createDebtViewModel.users.forEach { user ->
                        if (user.username == singleItems[which]) {
                            Picasso.with(this.context).load(user.avatar)
                                .into(binding.toUserAvatar)
                        }
                    }
                }
                .show()
        }
        binding.loginButton.setOnClickListener {
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
                    createDebtViewModel.users.forEach { user ->
                        if (user.username == binding.toUserName.text.toString()) {
                            userTo = user
                        }
                    }
                }
            }

            createDebtViewModel.createDebt(
                debtFor,
                debtAmount.toInt(),
                this.userFrom!!,
                userTo!!
            )
            createDebtViewModel.createDebtLiveData.observe(
                viewLifecycleOwner,
                { createDebtResponse ->
                    when (createDebtResponse.status) {
                        Status.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                createDebtResponse.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Status.LOADING -> {
                        }// TODO: 20.08.2021 Create progressBar
                        Status.SUCCESS -> {
                            Toast.makeText(
                                requireContext(),
                                "Debt was successfully created",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigateUp()
                        }
                    }
                })
        }
    }


    private fun getUsersFromGroup() {
        createDebtViewModel.group.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    createDebtViewModel.getUsersFromGroup(it.data!!)
                }
            }

        })
    }

    private fun getUserInfo() {
        createDebtViewModel.user.observe(
            viewLifecycleOwner,
            {
                when(it.status) {
                    Status.SUCCESS -> {
                        binding.fromUserName.setText(it.data!!.username)
                        this.userFrom = it.data
                        Picasso.with(this.context).load(it.data!!.avatar)
                            .into(binding.fromUserAvatar)
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    }
                }
            }
        )
    }
}