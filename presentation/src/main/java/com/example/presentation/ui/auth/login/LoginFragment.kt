package com.example.presentation.ui.auth.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.Avatar
import com.example.domain.models.Input
import com.example.domain.models.Status
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

  private var _binding: FragmentLoginBinding? = null
  private val binding
    get() = _binding!!

  private val loginViewModel: LoginViewModel by viewModels()

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentLoginBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loginButtonClicked()
    binding.dontHaveAnAccountText.setOnClickListener {
      findNavController()
        .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
    }
  }

  private fun loginButtonClicked() {
    val progressBar = binding.loginProgress
    progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
    progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))

    binding.loginButton.setOnClickListener {
      val email = binding.loginEmailEditText.text.toString()
      val password = binding.passwordEditText.text.toString()
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
          loginViewModel.userLiveData.observe(
            viewLifecycleOwner,
            { loginResponse ->
              when (loginResponse.status) {
                Status.ERROR -> {
                  if (loginResponse.message == "No user found") {
                      findNavController()
                          .navigate(LoginFragmentDirections.actionLoginFragmentToChooseNameFragment(Input(Avatar(Uri.EMPTY, false), email)))
                  } else {
                      progressBar.visibility = View.GONE
                      Toast.makeText(requireContext(), loginResponse.message, Toast.LENGTH_LONG).show()
                  }
                }
                Status.LOADING -> {
                  progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    findNavController()
                        .navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
                }
              }
            }
          )
        }
      }
    }
  }

  fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
  }
}
