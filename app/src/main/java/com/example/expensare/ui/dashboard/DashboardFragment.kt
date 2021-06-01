package com.example.expensare.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.example.expensare.databinding.FragmentDashboardBinding
import com.example.expensare.ui.auth.login.LoginFragmentDirections
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DashboardFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    lateinit var navigationView: NavigationView

    private val dashboardViewModel: DashboardViewModel by lazy { ViewModelProvider(this).get(DashboardViewModel::class.java) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLoggedIn()
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        navigationView = requireActivity().findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        binding.listButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left_chosen)
            binding.historyButton.setBackgroundResource(R.drawable.square_button)
            binding.historyRecyclerView.visibility = View.GONE
            binding.noHistoryText.visibility = View.GONE
            binding.noListText.visibility = View.VISIBLE
            binding.addNewItem.visibility = View.VISIBLE
            binding.finalizeButton.visibility = View.VISIBLE
            binding.addExpensesButton.visibility = View.GONE
        }

        binding.historyButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left)
            binding.historyButton.setBackgroundResource(R.drawable.square_button_chosen)
            binding.noHistoryText.visibility = View.VISIBLE
            binding.noListText.visibility = View.GONE
            binding.addNewItem.visibility = View.GONE
            binding.finalizeButton.visibility = View.GONE
            binding.addExpensesButton.visibility = View.VISIBLE
        }

        binding.addExpensesButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddExpensesFragment())
        }

        binding.debtButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToMyDebtsFragment())
        }

        binding.addNewItem.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddToListFragment())
        }

        binding.finalizeButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToFinishShopSessionFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        when(item.itemId) {
            R.id.dashboard -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.grocery_items -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSavedStoreFragment())
            }
            R.id.group_management -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToGroupManagementFragment())
            }
            R.id.change_group -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToChooseGroupFragment())
            }
            R.id.debt_request -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToRequestsViewPagerFragment())
            }
            R.id.settings -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSettingsFragment())
            }
            R.id.log_out -> {
                drawer.closeDrawer(GravityCompat.START)
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToLoginFragment())
            }
            else -> false
        }
        return true
    }

    private fun userLoggedIn() {
        if (FirebaseAuth.getInstance().uid == null) {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToLoginFragment())
        } else {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        dashboardViewModel.user.observe(viewLifecycleOwner, {
            if (it == null) {
                  findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToChooseNameFragment(
                      Avatar(Uri.EMPTY, false)
                  ))
            } else {
                val header = navigationView.getHeaderView(0)
                val drawerHeaderNameText = header.findViewById<MaterialTextView>(R.id.user_name)
                drawerHeaderNameText.text = it.username
                val drawerImageView = header.findViewById<CircleImageView>(R.id.user_avatar)
                Picasso.with(requireContext()).load(it.avatar).into(drawerImageView)
                val drawerStatusText = header.findViewById<MaterialTextView>(R.id.user_status)
                drawerStatusText.text = getString(R.string.owner)
            }
        })

        dashboardViewModel.group.observe(viewLifecycleOwner, {
            binding.absToolbarTitle.text = it.groupName
        })
    }
}