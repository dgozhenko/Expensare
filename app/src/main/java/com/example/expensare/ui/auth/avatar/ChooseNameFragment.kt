package com.example.expensare.ui.auth.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.databinding.FragmentNameRegistrationBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class ChooseNameFragment: BaseFragment() {
    private var _binding: FragmentNameRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentNameRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseAvatar.setOnClickListener {
            findNavController().navigate(ChooseNameFragmentDirections.actionChooseNameFragmentToAvatarPickerFragment())
        }

        binding.thatsMeButton.setOnClickListener {
            findNavController().navigate(ChooseNameFragmentDirections.actionChooseNameFragmentToDashboardFragment())
        }
    }
}