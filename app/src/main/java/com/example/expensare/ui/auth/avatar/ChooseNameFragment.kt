package com.example.expensare.ui.auth.avatar

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.example.expensare.databinding.FragmentNameRegistrationBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ChooseNameFragment: BaseFragment() {
    private var _binding: FragmentNameRegistrationBinding? = null
    private val binding get() = _binding!!

    private val chooseNameViewModel: ChooseNameViewModel by lazy { ViewModelProvider(this).get(ChooseNameViewModel::class.java) }

    val args: ChooseNameFragmentArgs by navArgs()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentNameRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.avatar.fromAvatarPicker) {
            binding.chooseAvatar.setImageURI(args.avatar.avatar)
        }
        binding.chooseAvatar.setOnClickListener {
            findNavController().navigate(ChooseNameFragmentDirections.actionChooseNameFragmentToAvatarPickerFragment())
        }

        binding.thatsMeButton.setOnClickListener {
            if (args.avatar.fromAvatarPicker) {
                val username = binding.nameEditText.text.toString()
                val avatar = args.avatar
                if (username.isNotEmpty()) {
                    chooseNameViewModel.uploadImage(avatar.avatar, username)
                    chooseNameViewModel.isComplete.observe(viewLifecycleOwner, {
                        if (it) {
                            findNavController().navigate(ChooseNameFragmentDirections.actionChooseNameFragmentToChooseGroupFragment())
                        } else {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    })


                }
            } else {
                Toast.makeText(requireContext(), "Choose avatar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}