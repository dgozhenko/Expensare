package com.example.expensare.ui.addexpense

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.expensare.R
import com.example.expensare.data.models.User
import com.example.expensare.databinding.DialogBottomSheetAddExpensesScreenBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddExpenseBottomSheetDialog(val amount: Int, val user: User): BottomSheetDialogFragment() {

    interface OnDivideMethodListener {
        fun onDivideMethodListener(divideAmount: Int, divideEqually: Boolean, user: User, startAmount: Int)
    }

    private var _binding: DialogBottomSheetAddExpensesScreenBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(requireContext(), R.layout.dialog_bottom_sheet_add_expenses_screen, null)
        _binding = DialogBottomSheetAddExpensesScreenBinding.bind(view)
        bottomSheet.setContentView(view)
        divideEquallyButtonClicked()
        divideExactAmount()
        return bottomSheet
    }

    private fun divideEquallyButtonClicked() {
        binding.equalButton.setOnClickListener {
            getDivideMethod(0, true, user, amount)
        }
    }

    private fun divideExactAmount() {
        binding.exactAmountButton.setOnClickListener {
            binding.equalButton.visibility = View.GONE
            binding.wannaPayTitle.visibility = View.GONE
            binding.amountTextLayout.visibility = View.VISIBLE
            binding.amountTitle.visibility = View.VISIBLE
            binding.setAmountButton.visibility = View.VISIBLE
            binding.exactAmountButton.visibility = View.GONE
            binding.setAmountButton.setOnClickListener {
                val amountTextEdit = binding.amountEditText.text.toString()
                if (amountTextEdit.isNotEmpty()) {
                    if (amountTextEdit.toInt() <= amount) {
                        getDivideMethod(amountTextEdit.toInt(), false, user, amount)
                    } else {
                        Toast.makeText(requireContext(), "Amount cannot be higher then expense", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Enter amount, please", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    private fun getDivideMethod(divideAmount: Int, divideEqually: Boolean, user: User, startAmount: Int) {
        val listener = targetFragment as OnDivideMethodListener?
        listener?.onDivideMethodListener(divideAmount, divideEqually, user, startAmount)
        dismiss()
    }
}