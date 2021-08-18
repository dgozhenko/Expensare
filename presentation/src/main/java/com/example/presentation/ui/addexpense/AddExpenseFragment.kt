package com.example.presentation.ui.addexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Debt
import com.example.domain.models.Status
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentAddExpensesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseFragment : BaseFragment(), AddExpenseBottomSheetDialog.OnDivideMethodListener {

  private var divideEqually: Boolean = false
  private var divideAmount: Int = 0
  private val fromUserId = arrayListOf<String>()
  private val debtsArray = arrayListOf<Debt>()
  private val equalDebtsArray = arrayListOf<Debt>()

  private var _binding: FragmentAddExpensesBinding? = null
  private val binding
    get() = _binding!!

  var context = this

  private val addExpenseViewModel: AddExpenseViewModel by viewModels()

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentAddExpensesBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getUsers()
    setupToolbar()
    setupRecyclerView()
  }

  override fun onDivideMethodListener(
    divideAmount: Int,
    divideEqually: Boolean,
    user: UserEntity,
    startAmount: Int
  ) {
    this.divideEqually = divideEqually
    var amount = startAmount
    this.divideAmount = amount

    addExpenseViewModel.user.observe(viewLifecycleOwner, {
        when (it.status) {
          Status.SUCCESS -> {
            if (divideEqually) {
              binding.progressBar.visibility = View.GONE
              val debt = Debt(it.data!!, user, divideAmount)
              equalDebtsArray.add(debt)
            } else {
              amount -= divideAmount
              val debt = Debt(it.data!!, user, divideAmount)
              debtsArray.add(debt)
              this.divideAmount = amount
            }
          }
          Status.ERROR -> {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
          }
          Status.LOADING -> {
            binding.progressBar.visibility = View.VISIBLE
          }
        }
      }
    )
  }

  private fun getUsers() {
    addExpenseViewModel.group.observe(
      viewLifecycleOwner,
      { result ->
        when (result.status) {
          Status.SUCCESS -> {
            binding.progressBar.visibility = View.GONE
            addExpenseViewModel.getUsersFromGroup(result.data!!)
          }
          Status.ERROR -> {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
          }
          Status.LOADING -> {
            binding.progressBar.visibility = View.VISIBLE
          }
        }
      }
    )
  }

  private fun setupRecyclerView() {
    val adapter =
      AddExpenseAdapter(
        OnClickListener {
          if (debtsArray.isNotEmpty() || equalDebtsArray.isNotEmpty()) {
            var noUserInDebtArray = true
            var noUserInEqualDebtArray = true

            debtsArray.forEach { debt ->
              if (debt.fromUser == debt.fromUser) {
                Toast.makeText(
                    requireContext(),
                    "You already added debt for this user",
                    Toast.LENGTH_SHORT
                  )
                  .show()
                noUserInDebtArray = false
              }
            }

            equalDebtsArray.forEach { equalDebt ->
              if (equalDebt.fromUser == equalDebt.fromUser) {
                Toast.makeText(
                    requireContext(),
                    "You already added debt for this user",
                    Toast.LENGTH_SHORT
                  )
                  .show()
                noUserInEqualDebtArray = false
              }
            }

            if (noUserInDebtArray && noUserInEqualDebtArray) {
              callDialog(it)
            }
          } else {
            callDialog(it)
          }
        }
      )
    binding.userRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    binding.userRecyclerView.adapter = adapter
    addExpenseViewModel.users.observe(viewLifecycleOwner, { adapter.getUsers(it) })
  }

  private fun callDialog(user: UserEntity) {
    val amountEditText = binding.amountEditText.text.toString()
    if (amountEditText.isNotEmpty()) {
      if (divideAmount == 0) {
        fromUserId.add(user.userUidId)
        val bottomSheetDialog = AddExpenseBottomSheetDialog(amountEditText.toInt(), user)
        bottomSheetDialog.setTargetFragment(this, 0)
        bottomSheetDialog.show(parentFragmentManager, "")
      } else {
        fromUserId.add(user.userUidId)
        val bottomSheetDialog = AddExpenseBottomSheetDialog(divideAmount, user)
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
          val nameEditText = binding.expensesEditText.text.toString()
          val amountEditText = binding.amountEditText.text.toString()

          when {
            nameEditText.isEmpty() -> {
              Toast.makeText(requireContext(), "Enter expense name, please", Toast.LENGTH_SHORT)
                .show()
              progressBar.visibility = View.GONE
            }
            amountEditText.isEmpty() -> {
              Toast.makeText(requireContext(), "Enter expense amount, please", Toast.LENGTH_SHORT)
                .show()
              progressBar.visibility = View.GONE
            }
            else -> {
              addExpenseViewModel.user.observe(viewLifecycleOwner, { userResponse ->
                  when(userResponse.status) {
                      Status.SUCCESS -> {
                          binding.progressBar.visibility = View.GONE
                          addExpenseViewModel.createExpense(
                              nameEditText,
                              amountEditText.toInt(),
                              userResponse.data!!,
                              true
                          )
                      }
                      Status.ERROR -> {
                          binding.progressBar.visibility = View.GONE
                          Toast.makeText(requireContext(), userResponse.message, Toast.LENGTH_SHORT).show()
                      }
                      Status.LOADING -> {
                          binding.progressBar.visibility = View.VISIBLE
                      }
                  }

                }
              )
              addExpenseViewModel.addExpenseResult.observe(
                viewLifecycleOwner,
                { result ->
                  when (result.status) {
                    Status.LOADING -> {
                      progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                      Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                      progressBar.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                      if (divideAmount == 0 && !divideEqually) {
                        progressBar.visibility = View.GONE
                      } else {
                        if (debtsArray.isNotEmpty()) {
                          debtsArray.forEach { debt ->
                            addExpenseViewModel.createDebt(debt.amount, debt.fromUser, debt.toUser)
                            addExpenseViewModel.addDebtResult.observe(
                              viewLifecycleOwner,
                              { debtResult ->
                                when (debtResult) {
                                  is AddDebtResult.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        debtResult.exception.message,
                                        Toast.LENGTH_SHORT
                                      )
                                      .show()
                                    progressBar.visibility = View.GONE
                                  }
                                  AddDebtResult.Success -> {
                                    progressBar.visibility = View.GONE
                                  }
                                }
                              }
                            )
                          }
                        }

                        if (equalDebtsArray.isNotEmpty()) {
                          equalDebtsArray.forEach { debt ->
                            val equalAmount = divideAmount / (1 + equalDebtsArray.size)
                            addExpenseViewModel.createDebt(equalAmount, debt.fromUser, debt.toUser)
                            addExpenseViewModel.addDebtResult.observe(
                              viewLifecycleOwner,
                              { debtResult ->
                                when (debtResult) {
                                  is AddDebtResult.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        debtResult.exception.message,
                                        Toast.LENGTH_SHORT
                                      )
                                      .show()
                                    progressBar.visibility = View.GONE
                                  }
                                  AddDebtResult.Success -> {
                                    progressBar.visibility = View.GONE
                                  }
                                }
                              }
                            )
                          }
                        }
                      }
                    }
                  }
                  findNavController().navigateUp()
                }
              )
            }
          }
          true
        }
        else -> false
      }
    }
  }
}
