package com.example.expensare.ui.auth.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.example.expensare.databinding.FragmentLoginBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth

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
        userLoggedIn()
        loginButtonClicked()
        binding.dontHaveAnAccountText.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
    }

    private fun userLoggedIn() {
        if (FirebaseAuth.getInstance().uid != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
        }
    }

    private fun loginButtonClicked() {
        val progressBar = binding.loginProgress
        progressBar.trackColor = resources.getColor(R.color.light_black)
        progressBar.setIndicatorColor(resources.getColor(R.color.red))

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            progressBar.visibility = View.VISIBLE
            it.hideKeyboard()
            when {
                email.isEmpty() -> {
                    Toast.makeText(requireContext(), "Missing e-mail", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(requireContext(), "Missing password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loginViewModel.loginUser(email, password)
                    loginViewModel.userLiveData.observe(viewLifecycleOwner, { user ->
                        if (user != null) {
                            progressBar.visibility = View.GONE
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
                        } else {
                            progressBar.visibility = View.GONE
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChooseNameFragment(
                                Avatar(Uri.EMPTY, false)
                            ))
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

    private fun crashTest() {
        binding.forgotPasswordText.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }

}