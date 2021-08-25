package com.example.presentation.ui.auth.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.presentation.ui.base.BaseFragment
import com.inner_circles_apps.myapplication.databinding.FragmentAvatarPickerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarPickerFragment: BaseFragment() {
    private var _binding: FragmentAvatarPickerBinding? = null
    private val binding get() = _binding!!

    private val avatarViewModel: AvatarViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAvatarPickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        val adapter = AvatarGridAdapter(OnClickListener {
            avatarViewModel.storeAvatar(it.avatar.toString())
            findNavController().navigate(AvatarPickerFragmentDirections.actionAvatarPickerFragmentToChooseNameFragment()
            )
        })
        binding.avatarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.avatarRecyclerView.adapter = adapter

        avatarViewModel.avatarList.observe(viewLifecycleOwner, {
            adapter.setAvatar(it)
        })
    }
}