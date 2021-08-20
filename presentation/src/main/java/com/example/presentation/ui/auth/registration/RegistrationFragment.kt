package com.example.presentation.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.util.Status
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.util.Extensions.hideKeyboard
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding
        get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by viewModels()

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
                    registrationViewModel.userLiveData.observe(
                        viewLifecycleOwner,
                        { registerResponse ->
                            when (registerResponse.status) {
                                Status.ERROR -> {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        requireContext(),
                                        registerResponse.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                Status.LOADING -> progressBar.visibility = View.VISIBLE
                                Status.SUCCESS -> findNavController().navigate(
                                    RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
                                )
                            }
                        })

                }
            }

        }
    }
}
