package com.example.misisapp.presentation.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.misisapp.R
import com.example.misisapp.data.session.SessionManager
import com.example.misisapp.databinding.ActivityMainBinding
import com.example.misisapp.domain.usecase.LogoutUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val rootDestinationsOrder = listOf(
        R.id.navigation_schedule,
        R.id.navigation_search,
        R.id.navigation_organizations,
        R.id.navigation_group,
        R.id.navigation_profile
    )

    @Inject
    lateinit var logoutUseCase: LogoutUseCase
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        observeSessionState()
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val currentFragment =
            findNavController(R.id.nav_host_fragment_activity_main).currentDestination?.id
        menu?.findItem(R.id.app_bar_calendar_button)?.isVisible =
            currentFragment == R.id.navigation_schedule
        menu?.findItem(R.id.app_bar_logout_button)?.isVisible =
            currentFragment == R.id.navigation_profile
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val currentDestination = navController.currentDestination?.id
        return when (currentDestination) {
            R.id.navigation_schedule -> {
                menuInflater.inflate(R.menu.app_bar_menu, menu)
                true
            }
            R.id.navigation_profile -> {
                menuInflater.inflate(R.menu.app_bar_menu, menu)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_calendar_button -> {
            showDatePickerDialog()
            true
        }

        R.id.app_bar_logout_button -> {
            performLogout()
            true
        }

        android.R.id.home -> {
            findNavController(R.id.nav_host_fragment_activity_main).navigateUp()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.navView

        // Setting up ActionBar (top bar)
        setSupportActionBar(findViewById(R.id.top_tool_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setting up AppBar (bottom bar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_schedule,
                R.id.navigation_search,
                R.id.navigation_organizations,
                R.id.navigation_group,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnItemSelectedListener { item ->
            val target = item.itemId
            val current = navController.currentDestination?.id ?: return@setOnItemSelectedListener true
            if (current == target) return@setOnItemSelectedListener true

            val curIndex   = rootDestinationsOrder.indexOf(current)
            val destIndex  = rootDestinationsOrder.indexOf(target)
            val forward    = destIndex > curIndex

            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)

                .setEnterAnim (if (forward) R.anim.slide_in_right  else R.anim.slide_in_left)
                .setExitAnim  (if (forward) R.anim.slide_out_left  else R.anim.slide_out_right)
                .setPopEnterAnim(if (forward) R.anim.slide_in_left  else R.anim.slide_in_right)
                .setPopExitAnim (if (forward) R.anim.slide_out_right else R.anim.slide_out_left)

                .setPopUpTo(navController.graph.id, false, saveState = true)
                .build()

            navController.navigate(target, null, navOptions)
            true
        }

        // When lessonDetails is on the screen, add back button in actionBar, else - remove one
        navController.addOnDestinationChangedListener { _, destination, _ ->
            this.invalidateOptionsMenu()
            navView.menu.findItem(destination.id)?.isChecked = true
            if (destination.id == R.id.navgiation_lesson_details) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_35)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_login -> {
                    supportActionBar?.hide()
                    navView.visibility = View.GONE
                }

                R.id.navigation_recover_password -> {
                    supportActionBar?.hide()
                    navView.visibility = View.GONE
                }

                else -> {
                    supportActionBar?.show()
                    navView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                sendToCurrentFragment(selectedDay, selectedMonth, selectedYear)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun sendToCurrentFragment(day: Int, month: Int, year: Int) {
        val bundle = Bundle().apply {
            putInt("day", day)
            putInt("month", month)
            putInt("year", year)
        }
        supportFragmentManager.setFragmentResult("date_selected", bundle)
    }

    private fun observeSessionState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sessionManager.unauthorized.collect {
                    navController.navigate(R.id.action_global_to_navigation_login)
                }
            }
        }
    }

    private fun performLogout() {
        lifecycleScope.launch {
            logoutUseCase()
        }
    }
}