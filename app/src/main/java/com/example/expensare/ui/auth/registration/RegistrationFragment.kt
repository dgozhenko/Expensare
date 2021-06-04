package com.example.expensare.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentRegistrationBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.util.Extensions.hideKeyboard
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
        progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
        progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordConfirm = binding.confirmPasswordEditText.text.toString()
            var errorFree = true
            progressBar.visibility = View.VISIBLE

            it.hideKeyboard()
            when {
                email.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.emailEditText.error = "Missing E-mail"
                }
                password.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.passwordEditText.error = "Missing password"
                }
                passwordConfirm.isEmpty() || passwordConfirm != password -> {
                    progressBar.visibility = View.GONE
                    binding.confirmPasswordEditText.error = "Passwords are not the same"
                }

                else -> {
                    registrationViewModel.registerUser(email, password)
                    registrationViewModel.error.observe(viewLifecycleOwner, { error ->
                        if (error != null) {
                            errorFree = false
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                    registrationViewModel.isRegisterComplete.observe(viewLifecycleOwner, { complete ->
                        if (errorFree) {
                            if (complete) {
                                progressBar.visibility = View.GONE
                                findNavController()
                                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                                Snackbar.make(this.requireView(), "Verification E-mail sent to you", Snackbar.LENGTH_SHORT).show()
                                registrationViewModel.registerCompleted()
                            }
                        } else {
                            registrationViewModel.errorCompleted()
                        }
                    })
                }
            }

        }
    }
}
