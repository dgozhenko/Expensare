package com.example.expensare.ui.auth.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentRegistrationBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

class RegistrationFragment : BaseFragment() {

  private var _binding: FragmentRegistrationBinding? = null
  private val binding
    get() = _binding!!

  private val registrationViewModel: RegistrationViewModel by lazy {
    ViewModelProvider(this).get(RegistrationViewModel::class.java)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentRegistrationBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
      registrationButtonClicked()
    binding.alreadyHaveAnAccountText.setOnClickListener {
      findNavController()
          .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
    }
  }

    private fun registrationButtonClicked() {
        val progressBar = binding.registrationProgress
        progressBar.trackColor = resources.getColor(R.color.light_black)
        progressBar.setIndicatorColor(resources.getColor(R.color.red))

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordConfirm = binding.confirmPasswordEditText.text.toString()
            progressBar.visibility = View.VISIBLE

            it.hideKeyboard()
            when {
                email.isEmpty() -> {
                    Toast.makeText(context, "Missing e-mail", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(context, "Missing username", Toast.LENGTH_SHORT).show()
                }
                passwordConfirm.isEmpty() || passwordConfirm != password -> {
                    Toast.makeText(context, "Passwords are not the same", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    registrationViewModel.registerUser(email, password)
                    registrationViewModel.isRegisterComplete.observe(viewLifecycleOwner, { complete ->
                        if (complete) {
                            progressBar.visibility = View.GONE
                            findNavController()
                                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                            Snackbar.make(this.requireView(), "Verification E-mail sent to you", Snackbar.LENGTH_SHORT).show()
                            registrationViewModel.registerCompleted()
                        }
                    })

                }
            }

        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
