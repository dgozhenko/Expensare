package com.example.presentation.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.inner_circles_apps.myapplication.databinding.DialogEmailBinding


class AddUserByEmailDialog: DialogFragment() {

    var listener: Listener? = null
    private var _binding: DialogEmailBinding? = null
    val binding get() = _binding!!

    interface Listener {
        fun onDialogButtonClicked(add: Boolean, email: String?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEmailBinding.inflate(LayoutInflater.from(requireContext()))
        val alert = AlertDialog.Builder(requireContext()).setView(binding.root)
            .setPositiveButton("Add to group") {_, _ ->
                val email = binding.itemNameEditText.text.toString()
                listener?.onDialogButtonClicked(true, email)
            }
            .setNeutralButton("Cancel") {_, _ ->
                listener?.onDialogButtonClicked(false, null)
            }
            .create()
        alert.setContentView(binding.root)

        return alert
        }

}