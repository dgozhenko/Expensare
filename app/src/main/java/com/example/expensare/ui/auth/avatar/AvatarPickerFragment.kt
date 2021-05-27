package com.example.expensare.ui.auth.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.example.expensare.databinding.FragmentAvatarPickerBinding
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class AvatarPickerFragment: BaseFragment() {
    private var _binding: FragmentAvatarPickerBinding? = null
    private val binding get() = _binding!!

    private val avatarViewModel: AvatarViewModel by lazy { ViewModelProvider(this).get(AvatarViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAvatarPickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        val adapter = AvatarGridAdapter(OnClickListener {
            findNavController().navigate(AvatarPickerFragmentDirections.actionAvatarPickerFragmentToChooseNameFragment(it))
        })
        binding.avatarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.avatarRecyclerView.adapter = adapter

        avatarViewModel.avatarList.observe(viewLifecycleOwner, {
            adapter.setAvatar(it)
        })
    }
}