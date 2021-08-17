package com.example.expensare.ui.auth.avatar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensare.App
import com.example.expensare.data.models.Input
import com.example.expensare.databinding.FragmentAvatarPickerBinding
import com.example.expensare.ui.addexpense.AddExpenseViewModel
import com.example.expensare.ui.base.BaseFragment
import javax.inject.Inject

class AvatarPickerFragment: BaseFragment() {
    private var _binding: FragmentAvatarPickerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val avatarViewModel by viewModels<AvatarViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentAvatarPickerBinding.inflate(inflater)
        return binding.root
    }

    private val args: AvatarPickerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.absToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        val adapter = AvatarGridAdapter(OnClickListener {
            findNavController().navigate(AvatarPickerFragmentDirections.actionAvatarPickerFragmentToChooseNameFragment(
                Input(it, args.email)
            ))
        })
        binding.avatarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.avatarRecyclerView.adapter = adapter

        avatarViewModel.avatarList.observe(viewLifecycleOwner, {
            adapter.setAvatar(it)
        })
    }
}