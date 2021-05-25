package com.example.expensare.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
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
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    Toast.makeText(requireContext(), "Missing e-mail", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(requireContext(), "Missing password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loginViewModel.loginUser(email, password)
                    loginViewModel.userLiveData.observe(viewLifecycleOwner, {
                        if (it != null) {
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())

                        } else {
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChooseNameFragment())
                        }
                    })
                }
            }
        }
    }
}