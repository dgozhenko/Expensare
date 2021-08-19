package com.example.presentation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Status
import com.example.presentation.ui.base.BaseFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.inner_circles_apps.myapplication.R
import com.inner_circles_apps.myapplication.databinding.FragmentDashboardBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class DashboardFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {

  var context = this

  private var _binding: FragmentDashboardBinding? = null
  private val binding
    get() = _binding!!

  private var _adapter: DashboardExpenseAdapter? = null
  private val adapter
    get() = _adapter!!

  private var _navigationView: NavigationView? = null
  private val navigationView
    get() = _navigationView!!

  private val dashboardViewModel: DashboardViewModel by viewModels()

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
    _binding = FragmentDashboardBinding.inflate(inflater)
    _navigationView = requireActivity().findViewById(R.id.test_nav_view)
    getUserInfo()
    bindToolbarAndNavDrawer()
    bindExpensesRecyclerView()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindButtons()
  }

  override fun onResume() {
    super.onResume()
    dashboardViewModel.refreshExpenses()
    dashboardViewModel.refreshedExpenses.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          Status.SUCCESS -> {
            binding.progressCircular.visibility = View.GONE
            adapter.getExpenses(it.data!!)
          }
          Status.ERROR -> {
            binding.progressCircular.visibility = View.GONE
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
          }
          Status.LOADING -> {
            binding.progressCircular.visibility = View.VISIBLE
          }
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
        when (it.status) {
          Status.SUCCESS -> {
            binding.noHistoryText.visibility = View.GONE
            binding.progressCircular.visibility = View.GONE
            adapter.getExpenses(it.data!!)
          }
          Status.ERROR -> {
            binding.noHistoryText.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
          }
          Status.LOADING -> {
            binding.progressCircular.visibility = View.VISIBLE
          }
        }
      }
    )
  }

  private fun bindToolbarAndNavDrawer() {
    val toolbar = binding.absToolbar
    val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)
    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
    val navigationView: NavigationView = requireActivity().findViewById(R.id.test_nav_view)
    navigationView.setNavigationItemSelectedListener(this)
  }

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
        .navigate(DashboardFragmentDirections.actionDashboardFragmentToAddExpensesFragment())
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val drawer = requireActivity().findViewById<DrawerLayout>(R.id.test_drawer_layout)
    when (item.itemId) {
      R.id.dashboard -> {
        drawer.closeDrawer(GravityCompat.START)
      }
      R.id.grocery_items -> {
        drawer.closeDrawer(GravityCompat.START)
        findNavController()
          .navigate(DashboardFragmentDirections.actionDashboardFragmentToSavedStoreFragment())
      }
      R.id.group_management -> {
        drawer.closeDrawer(GravityCompat.START)
        findNavController()
          .navigate(DashboardFragmentDirections.actionDashboardFragmentToGroupManagementFragment())
      }
      R.id.change_group -> {
        drawer.closeDrawer(GravityCompat.START)
        findNavController()
          .navigate(DashboardFragmentDirections.actionDashboardFragmentToChooseGroupFragment())
      }
      R.id.debt_request -> {
        drawer.closeDrawer(GravityCompat.START)
        findNavController()
          .navigate(
            DashboardFragmentDirections.actionDashboardFragmentToRequestsViewPagerFragment()
          )
      }
      R.id.settings -> {
        drawer.closeDrawer(GravityCompat.START)
        findNavController()
          .navigate(DashboardFragmentDirections.actionDashboardFragmentToSettingsFragment())
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
        when (it.status) {
          Status.ERROR -> {
            binding.progressCircular.visibility = View.GONE
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            findNavController()
              .navigate(DashboardFragmentDirections.actionDashboardFragmentToLoginFragment())
            FirebaseAuth.getInstance().signOut()
          }
          Status.SUCCESS -> {
            dashboardViewModel.getGroupByGroupId()
            binding.progressCircular.visibility = View.GONE
            val header = navigationView.getHeaderView(0)
            val drawerHeaderNameText = header.findViewById<MaterialTextView>(R.id.user_name)
            drawerHeaderNameText.text = it.data!!.username
            val drawerImageView = header.findViewById<CircleImageView>(R.id.user_avatar)

            Picasso.with(requireContext()).load(it.data!!.avatar).into(drawerImageView)
            val drawerStatusText = header.findViewById<MaterialTextView>(R.id.user_status)
            drawerStatusText.text = getString(R.string.owner)
          }
          Status.LOADING -> {
            binding.progressCircular.visibility = View.VISIBLE
          }
        }
      }
    )

    dashboardViewModel.group.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          Status.SUCCESS -> {
            binding.progressCircular.visibility = View.GONE
            binding.absToolbarTitle.text = it.data!!.groupName
            dashboardViewModel.getGroupExpenses()
          }
          Status.ERROR -> {
            binding.progressCircular.visibility = View.GONE
            findNavController()
              .navigate(DashboardFragmentDirections.actionDashboardFragmentToChooseGroupFragment())
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
          }
          Status.LOADING -> {
            binding.progressCircular.visibility = View.VISIBLE
          }
        }
      }
    )
  }
}
