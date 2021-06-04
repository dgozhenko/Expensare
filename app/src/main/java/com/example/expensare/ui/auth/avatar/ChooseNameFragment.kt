package com.example.expensare.ui.auth.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensare.R
import com.example.expensare.databinding.FragmentNameRegistrationBinding
import com.example.expensare.ui.base.BaseFragment
import com.example.expensare.util.Extensions.hideKeyboard

class ChooseNameFragment : BaseFragment() {
    private var _binding: FragmentNameRegistrationBinding? = null
    private val binding
        get() = _binding!!

    private val chooseNameViewModel: ChooseNameViewModel by lazy {
        ViewModelProvider(this).get(ChooseNameViewModel::class.java)
    }

    private val args: ChooseNameFragmentArgs by navArgs()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentNameRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
        if (args.input.Avatar.fromAvatarPicker) {
            binding.chooseAvatar.setImageURI(args.input.Avatar.avatar)
        }
    }

    private fun bindButtons() {
        val progressBar = binding.progressBar
        progressBar.trackColor = resources.getColor(R.color.light_black, requireActivity().theme)
        progressBar.setIndicatorColor(resources.getColor(R.color.red, requireActivity().theme))

        binding.thatsMeButton.setOnClickListener {
            it.hideKeyboard()
            progressBar.visibility = View.VISIBLE
            if (args.input.Avatar.fromAvatarPicker) {
                val username = binding.nameEditText.text.toString()
                val avatar = args.input.Avatar
                if (username.isNotEmpty()) {
                    chooseNameViewModel.uploadImage(avatar.avatar, username, args.input.email)
                    chooseNameViewModel.chooseNameResult.observe(viewLifecycleOwner, { result ->
                        when (result) {
                            is ChooseNameResult.Error -> {
                                Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                            }
                            ChooseNameResult.Success -> {
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
        }

        binding.chooseAvatar.setOnClickListener {
            it.hideKeyboard()
            findNavController()
                .navigate(
                    ChooseNameFragmentDirections.actionChooseNameFragmentToAvatarPickerFragment(args.input.email)
                )
        }
    }
}
