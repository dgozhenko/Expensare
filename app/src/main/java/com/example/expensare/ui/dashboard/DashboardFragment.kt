package com.example.expensare.ui.dashboard

import android.content.Context
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
import com.example.expensare.App
import com.example.expensare.R
import com.example.expensare.databinding.FragmentDashboardBinding
import com.example.expensare.ui.auth.avatar.AvatarViewModel
import com.example.expensare.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class DashboardFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var navigationView: NavigationView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val dashboardViewModel by viewModels<DashboardViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLoggedIn()
        bindToolbarAndNavDrawer()
        bindButtons()
    }

    override fun onResume() {
        super.onResume()
        // TO-DO Add real-time update
        bindExpensesRecyclerView()
    }

    private fun bindExpensesRecyclerView() {
        val adapter = DashboardExpenseAdapter()
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter
        dashboardViewModel.expense.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.noHistoryText.visibility = View.GONE
                adapter.getExpenses(it)
            } else {
                binding.noHistoryText.visibility = View.VISIBLE
            }
        })
    }

    private fun bindToolbarAndNavDrawer() {
        val toolbar = binding.absToolbar
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
        navigationView = requireActivity().findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun bindButtons() {
        binding.listButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left_chosen)
            binding.historyButton.setBackgroundResource(R.drawable.square_button)
            binding.historyRecyclerView.visibility = View.GONE
            binding.noHistoryText.visibility = View.GONE
            binding.noListText.visibility = View.VISIBLE
            binding.addNewItem.visibility = View.VISIBLE
            binding.finalizeButton.visibility = View.VISIBLE
            binding.addExpensesButton.visibility = View.GONE
            binding.groceryListRecyclerView.visibility = View.VISIBLE
        }

        binding.historyButton.setOnClickListener {
            binding.listButton.setBackgroundResource(R.drawable.rounded_button_left)
            binding.historyButton.setBackgroundResource(R.drawable.square_button_chosen)
            binding.noHistoryText.visibility = View.VISIBLE
            binding.noListText.visibility = View.GONE
            binding.addNewItem.visibility = View.GONE
            binding.finalizeButton.visibility = View.GONE
            binding.addExpensesButton.visibility = View.VISIBLE
            binding.historyRecyclerView.visibility = View.VISIBLE
            binding.groceryListRecyclerView.visibility = View.GONE
            bindExpensesRecyclerView()
        }

        binding.debtButton.setOnClickListener {
            findNavController()
                .navigate(DashboardFragmentDirections.actionDashboardFragmentToMyDebtsFragment())
        }

        binding.addNewItem.setOnClickListener {
            findNavController()
                .navigate(DashboardFragmentDirections.actionDashboardFragmentToAddToListFragment())
        }

        binding.finalizeButton.setOnClickListener {
            findNavController()
                .navigate(
                    DashboardFragmentDirections.actionDashboardFragmentToFinishShopSessionFragment()
                )
        }

        binding.addExpensesButton.setOnClickListener {
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

    private fun userLoggedIn() {
        if (FirebaseAuth.getInstance().uid == null) {
            findNavController()
                .navigate(DashboardFragmentDirections.actionDashboardFragmentToLoginFragment())
        } else {
            getUserInfo()
        }
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
                    val header = navigationView.getHeaderView(0)
                    val drawerHeaderNameText = header.findViewById<MaterialTextView>(R.id.user_name)
                    drawerHeaderNameText.text = it.username
                    val drawerImageView = header.findViewById<CircleImageView>(R.id.user_avatar)
                    Picasso.with(requireContext()).load(it.avatar).into(drawerImageView)
                    val drawerStatusText = header.findViewById<MaterialTextView>(R.id.user_status)
                    drawerStatusText.text = getString(R.string.owner)
                }
            }
        )

        dashboardViewModel.group.observe(
            viewLifecycleOwner,
            { binding.absToolbarTitle.text = it.groupName }
        )
    }

}
