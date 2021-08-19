package com.example.presentation.ui.auth.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.ui.base.BaseFragment
import com.example.presentation.util.Extensions.hideKeyboard
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentNameRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseNameFragment : BaseFragment() {
    private var _binding: FragmentNameRegistrationBinding? = null
    private val binding
        get() = _binding!!

    private val chooseNameViewModel: ChooseNameViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentNameRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
        chooseNameViewModel.avatar.observe(viewLifecycleOwner, {
            if (it != "def" && it.isNotBlank()) {
                binding.chooseAvatar.setImageURI(it.toUri())
            }
        })
    }

    private fun bindButtons() {
        val progressBar = binding.progressBar
        progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
        progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))

        binding.thatsMeButton.setOnClickListener {
            var email = ""
            chooseNameViewModel.email.observe(viewLifecycleOwner, {emailString ->
                email = emailString
            })
            it.hideKeyboard()
            progressBar.visibility = View.VISIBLE
            chooseNameViewModel.avatar.observe(viewLifecycleOwner, {avatarString ->
                if (avatarString != "def" && avatarString.isNotBlank()) {
                    val username = binding.nameEditText.text.toString()
                    if (username.isNotEmpty()) {
                        chooseNameViewModel.uploadImage(avatarString.toUri(), username, email)
                        chooseNameViewModel.chooseNameResult.observe(viewLifecycleOwner, { result ->
                            when (result) {
                                is ChooseNameResult.Error -> {
                                    Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                                    progressBar.visibility = View.GONE
                                }
                                ChooseNameResult.Success -> {
                                    chooseNameViewModel.deleteStoredAvatar()
                                    chooseNameViewModel.deleteStoredEmail()
                                    findNavController()
                                        .navigate(
                                            ChooseNameFragmentDirections
                                                .actionChooseNameFragmentToChooseGroupFragment())
                                    progressBar.visibility = View.GONE
                                }
                            }
                        })
                    } else {
                        Toast.makeText(requireContext(), "How should we call you?", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(requireContext(), "Choose avatar", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            })
        }

        binding.chooseAvatar.setOnClickListener {
            it.hideKeyboard()
            findNavController()
                .navigate(
                    ChooseNameFragmentDirections.actionChooseNameFragmentToAvatarPickerFragment()
                )
        }
    }
}
