package com.example.expensare.ui.auth.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.example.expensare.data.Input
import com.example.expensare.databinding.FragmentLoginBinding
import com.example.expensare.ui.base.BaseFragment
class LoginFragment: BaseFragment() {



    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButtonClicked()
        binding.dontHaveAnAccountText.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
    }

    private fun loginButtonClicked() {
        val progressBar = binding.loginProgress
        progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
        progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            var errorFree = false
            var verificationErrorFree = false
            progressBar.visibility = View.VISIBLE
            it.hideKeyboard()
            when {
                email.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.loginEmailEditText.error = "Missing E-mail"
                }
                password.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.passwordEditText.error = "Missing password"
                }
                else -> {
                    loginViewModel.loginUser(email, password)
                    loginViewModel.verificationError.observe(viewLifecycleOwner, { verificationError ->
                        if (verificationError != null) {
                            if (verificationError) {
                                progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Verify E-mail was sent", Toast.LENGTH_SHORT).show()
                            } else {
                                verificationErrorFree = true
                            }
                            loginViewModel.verificationErrorComplete()
                        }
                    })
                    loginViewModel.error.observe(viewLifecycleOwner, { error ->
                        if (error != null) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                            loginViewModel.errorComplete()
                        }  else {
                            errorFree = true
                        }
                    })
                    loginViewModel.userLiveData.observe(viewLifecycleOwner, { user ->
                        if (verificationErrorFree) {
                            if (errorFree) {
                                if (user == true) {
                                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
                                } else {
                                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChooseNameFragment(
                                        Input(Avatar(Uri.EMPTY, false), email)))
                                }
                            } else {
                                loginViewModel.errorComplete()
                            }
                        } else {
                            loginViewModel.verificationErrorComplete()
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