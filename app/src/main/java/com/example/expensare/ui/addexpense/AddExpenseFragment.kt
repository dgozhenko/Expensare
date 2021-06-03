package com.example.expensare.ui.addexpense

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensare.R
import com.example.expensare.data.Debt
import com.example.expensare.data.User
import com.example.expensare.databinding.FragmentAddExpensesBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth


class AddExpenseFragment: BaseFragment(), AddExpenseBottomSheetDialog.OnDivideMethodListener {

    private var divideEqually: Boolean = false
    private var divideAmount: Int = 0
    private val fromUserId = arrayListOf<String>()
    private val debtsArray = arrayListOf<Debt>()
    private val equalDebtsArray = arrayListOf<Debt>()

    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!

    private val addExpenseViewModel: AddExpenseViewModel by lazy { ViewModelProvider(this).get(AddExpenseViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAddExpensesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsers()
        setupToolbar()
        setupRecyclerView()
        Log.d("TAGGGG", "created")
    }

    override fun onDivideMethodListener(
        divideAmount: Int,
        divideEqually: Boolean,
        user: User,
        startAmount: Int
    ) {
        this.divideEqually = divideEqually
        val uid = FirebaseAuth.getInstance().uid
        var amount = startAmount
        this.divideAmount = amount
        if (divideEqually) {
            val debt = Debt(uid!!, user.uid, amount )
            equalDebtsArray.add(debt)
        } else {
            amount -= divideAmount
            val debt = Debt(uid!!, user.uid, divideAmount)
            debtsArray.add(debt)
            this.divideAmount = amount
        }

    }

    private fun getUsers() {
        addExpenseViewModel.group.observe(viewLifecycleOwner, {
            addExpenseViewModel.getUsersFromGroup(it)
        })
    }

    private fun setupRecyclerView() {
        val adapter = AddExpenseAdapter(OnClickListener {
            val amountEditText = binding.amountEditText.text.toString()
            if (amountEditText.isNotEmpty()) {
                if (divideAmount == 0) {
                    fromUserId.add(it.uid)
                    val bottomSheetDialog  =  AddExpenseBottomSheetDialog(amountEditText.toInt(), it)
                    bottomSheetDialog.setTargetFragment(this, 0)
                    bottomSheetDialog.show(parentFragmentManager, "")
                } else {
                    fromUserId.add(it.uid)
                    val bottomSheetDialog  =  AddExpenseBottomSheetDialog(divideAmount, it)
                    bottomSheetDialog.setTargetFragment(this, 0)
                    bottomSheetDialog.show(parentFragmentManager, "")
                }

            } else {
                Toast.makeText(requireContext(), "Enter amount for dividing first", Toast.LENGTH_SHORT).show()
            }

        })
        binding.userRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.userRecyclerView.adapter = adapter
        addExpenseViewModel.users.observe(viewLifecycleOwner, {
            adapter.getUsers(it)
        })
    }

    private fun setupToolbar() {
        val progressBar = binding.progressBar
        progressBar.trackColor = resources.getColor(R.color.light_black)
        progressBar.setIndicatorColor(resources.getColor(R.color.red))
        binding.absToolbar.inflateMenu(R.menu.add_expenses_menu)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.absToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_expenses_button -> {
                    progressBar.visibility = View.VISIBLE
                    val nameEditText = binding.expensesEditText.text.toString()
                    val amountEditText = binding.amountEditText.text.toString()
                    when {
                        nameEditText.isEmpty() -> {
                            Toast.makeText(
                                requireContext(),
                                "Enter expense name, please",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progressBar.visibility = View.GONE
                        }
                        amountEditText.isEmpty() -> {
                            Toast.makeText(
                                requireContext(),
                                "Enter expense amount, please",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progressBar.visibility = View.GONE
                        }
                        else -> {
                            addExpenseViewModel.user.observe(viewLifecycleOwner, { user ->
                                addExpenseViewModel.createExpense(nameEditText, amountEditText.toInt(), user)
                            })
                            addExpenseViewModel.addExpenseResult.observe(viewLifecycleOwner, { result ->
                                when (result) {
                                    is AddExpenseResult.Error -> {
                                        Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                                        progressBar.visibility = View.GONE
                                    }
                                    AddExpenseResult.Success -> {
                                        if (divideAmount == 0 && !divideEqually) {
                                            progressBar.visibility = View.GONE
                                            findNavController().navigateUp()
                                        } else {
                                            if (debtsArray.isEmpty()) {

                                            } else {
                                                debtsArray.forEach { debt ->
                                                addExpenseViewModel.createDebt(debt.amount, debt.from)
                                                addExpenseViewModel.addDebtResult.observe(viewLifecycleOwner, { debtResult ->
                                                    when (debtResult) {
                                                        is AddDebtResult.Error -> {
                                                            Toast.makeText(requireContext(), debtResult.exception.message, Toast.LENGTH_SHORT).show()
                                                            progressBar.visibility = View.GONE
                                                        }
                                                        AddDebtResult.Success -> {

                                                        }
                                                    }
                                                })

                                            }
                                            }

                                            if (equalDebtsArray.isEmpty()) {

                                            } else {
                                                equalDebtsArray.forEach { debt ->
                                                    val equalAmount = divideAmount / (1 + equalDebtsArray.size)
                                                    addExpenseViewModel.createDebt(equalAmount, debt.from)
                                                    addExpenseViewModel.addDebtResult.observe(viewLifecycleOwner, { debtResult ->
                                                        when (debtResult) {
                                                            is AddDebtResult.Error -> {
                                                                Toast.makeText(requireContext(), debtResult.exception.message, Toast.LENGTH_SHORT).show()
                                                                progressBar.visibility = View.GONE
                                                            }
                                                            AddDebtResult.Success -> {
                                                                progressBar.visibility = View.GONE
                                                            }
                                                        }
                                                    })

                                                }
                                            }
                                            findNavController().navigateUp()
                                        }
                                    }
                                }
                            })
                        }
                    }
                    true
                } else -> false
            }
        }
    }
}