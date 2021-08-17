package com.example.presentation.ui.dashboard

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {

    var context = this
    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null

    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    private var _adapter: DashboardExpenseAdapter? = null
    private val adapter
        get() = _adapter!!

    private lateinit var navigationView: NavigationView
    private var userExists: Boolean = false

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDashboardBinding.inflate(inflater)
        //bindToolbarAndNavDrawer()
        getUserInfo()
        bindExpensesRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // FIXME: 09.08.2021 c
        bindButtons()
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.refreshExpenses()
        dashboardViewModel.refreshedExpenses.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    adapter.getExpenses(it)
               }
            }
        )
    }

    private fun bindExpensesRecyclerView() {
        binding.progressCircular.visibility = View.VISIBLE
        _adapter = DashboardExpenseAdapter()
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter

        dashboardViewModel.expense.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    binding.noHistoryText.visibility = View.GONE
                    adapter.getExpenses(it)
                    binding.progressCircular.visibility = View.GONE
                } else {
                    binding.noHistoryText.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        )
    }

//    private fun bindToolbarAndNavDrawer() {
//        val toolbar = binding.absToolbar
//        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
//        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
//        navigationView = requireActivity().findViewById(R.id.navigation_view)
//        navigationView.setNavigationItemSelectedListener(this)
//    }

    private fun bindButtons() {
        binding.listButton.setBackgroundResource(R.drawable.rounded_button_left)
        binding.historyButton.setBackgroundResource(R.drawable.square_button_chosen)
        binding.debtButton.setBackgroundResource(R.drawable.rounded_button_right)

        binding.listButton.setOnClickListener {
            findNavController()
                .navigate(DashboardFragmentDirections.actionDashboardFragmentToListFragment())
        }

        binding.debtButton.setOnClickListener {
            findNavController()
                .navigate(DashboardFragmentDirections.actionDashboardFragmentToMyDebtsFragment())
        }

        binding.addExpenses.setOnClickListener {
            findNavController()
                .navigate(
                    DashboardFragmentDirections.actionDashboardFragmentToAddExpensesFragment()
                )
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        when (item.itemId) {
            R.id.dashboard -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.grocery_items -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController()
                    .navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToSavedStoreFragment()
                    )
            }
            R.id.group_management -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController()
                    .navigate(
                        DashboardFragmentDirections
                            .actionDashboardFragmentToGroupManagementFragment()
                    )
            }
            R.id.change_group -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController()
                    .navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToChooseGroupFragment()
                    )
            }
            R.id.debt_request -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController()
                    .navigate(
                        DashboardFragmentDirections
                            .actionDashboardFragmentToRequestsViewPagerFragment()
                    )
            }
            R.id.settings -> {
                drawer.closeDrawer(GravityCompat.START)
                findNavController()
                    .navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToSettingsFragment()
                    )
            }
            R.id.log_out -> {
                drawer.closeDrawer(GravityCompat.START)
                FirebaseAuth.getInstance().signOut()
                findNavController()
                    .navigate(DashboardFragmentDirections.actionDashboardFragmentToLoginFragment())
            }
        }
        return true
    }

    private fun getUserInfo() {
        dashboardViewModel.user.observe(
            viewLifecycleOwner,
            {
                if (it == null) {
                    findNavController()
                        .navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
                        )
                    FirebaseAuth.getInstance().signOut()
                } else {
//                    val header = navigationView.getHeaderView(0)
//                    val drawerHeaderNameText = header.findViewById<MaterialTextView>(R.id.user_name)
//                    drawerHeaderNameText.text = it.username
//                    val drawerImageView = header.findViewById<CircleImageView>(R.id.user_avatar)
//                    Picasso.with(requireContext()).load(it.avatar).into(drawerImageView)
//                    val drawerStatusText = header.findViewById<MaterialTextView>(R.id.user_status)
//                    drawerStatusText.text = getString(R.string.owner)
                }
            }
        )

        dashboardViewModel.group.observe(
            viewLifecycleOwner,
            { binding.absToolbarTitle.text = it.groupName }
        )
    }
}