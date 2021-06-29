package com.example.expensare.ui.addexpense

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensare.App
import com.example.expensare.R
import com.example.expensare.data.models.Debt
import com.example.expensare.data.models.User
import com.example.expensare.databinding.FragmentAddExpensesBinding
import com.example.expensare.ui.base.BaseFragment
import javax.inject.Inject


class AddExpenseFragment: BaseFragment(), AddExpenseBottomSheetDialog.OnDivideMethodListener {

    private var divideEqually: Boolean = false
    private var divideAmount: Int = 0
    private val fromUserId = arrayListOf<String>()
    private val debtsArray = arrayListOf<Debt>()
    private val equalDebtsArray = arrayListOf<Debt>()

    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val addExpenseViewModel by viewModels<AddExpenseViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAddExpensesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsers()
        setupToolbar()
        setupRecyclerView()
        Log.d("TAG", "created")
    }

    override fun onDivideMethodListener(
        divideAmount: Int,
        divideEqually: Boolean,
        user: User,
        startAmount: Int
    ) {
        this.divideEqually = divideEqually
        var amount = startAmount
        this.divideAmount = amount

        if (divideEqually) {
            addExpenseViewModel.user.observe(viewLifecycleOwner, {
                val debt = Debt(it, user, divideAmount)
                equalDebtsArray.add(debt)
            })
        } else {
            addExpenseViewModel.user.observe(viewLifecycleOwner, {
                amount -= divideAmount
                val debt = Debt(it, user, divideAmount)
                debtsArray.add(debt)
                this.divideAmount = amount
            })
        }

    }

    private fun getUsers() {
        addExpenseViewModel.group.observe(viewLifecycleOwner, {
            addExpenseViewModel.getUsersFromGroup(it)
        })
    }

    private fun setupRecyclerView() {
        val adapter = AddExpenseAdapter(OnClickListener {
            if (debtsArray.isNotEmpty() || equalDebtsArray.isNotEmpty()) {
                var noUserInDebtArray = true
                var noUserInEqualDebtArray = true

                debtsArray.forEach { debt->
                    if (debt.fromUser == it) {
                        Toast.makeText(requireContext(), "You already added debt for this user", Toast.LENGTH_SHORT).show()
                        noUserInDebtArray = false
                    }
                }

                equalDebtsArray.forEach {equalDebt ->
                    if (equalDebt.fromUser == it) {
                        Toast.makeText(requireContext(), "You already added debt for this user", Toast.LENGTH_SHORT).show()
                        noUserInEqualDebtArray = false
                    }
                }

                if (noUserInDebtArray && noUserInEqualDebtArray) {
                    callDialog(it)
                }


            } else {
                callDialog(it)
            }
        })
        binding.userRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.userRecyclerView.adapter = adapter
        addExpenseViewModel.users.observe(viewLifecycleOwner, {
            adapter.getUsers(it)
        })
    }

    private fun callDialog(user: User) {
        val amountEditText = binding.amountEditText.text.toString()
        if (amountEditText.isNotEmpty()) {
            if (divideAmount == 0) {
                fromUserId.add(user.uid)
                val bottomSheetDialog  =  AddExpenseBottomSheetDialog(amountEditText.toInt(), user)
                bottomSheetDialog.setTargetFragment(this, 0)
                bottomSheetDialog.show(parentFragmentManager, "")
            } else {
                fromUserId.add(user.uid)
                val bottomSheetDialog  =  AddExpenseBottomSheetDialog(divideAmount, user)
                bottomSheetDialog.setTargetFragment(this, 0)
                bottomSheetDialog.show(parentFragmentManager, "")
            }

        } else {
            Toast.makeText(requireContext(), "Enter amount for dividing first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        val progressBar = binding.progressBar
        progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
        progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))
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
                                            if (debtsArray.isNotEmpty()) {
                                                debtsArray.forEach { debt ->
                                                    addExpenseViewModel.createDebt(debt.amount, debt.fromUser, debt.toUser)
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

                                            if (equalDebtsArray.isNotEmpty()) {
                                                equalDebtsArray.forEach { debt ->
                                                    val equalAmount = divideAmount / (1 + equalDebtsArray.size)
                                                    addExpenseViewModel.createDebt(equalAmount, debt.fromUser, debt.toUser)
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