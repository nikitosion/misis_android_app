package com.example.misisapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.misisapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.top_tool_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_schedule, R.id.navigation_search, R.id.navigation_organizations, R.id.navigation_group, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    supportActionBar?.hide()
                    navView.visibility = View.GONE
                }
                R.id.recoverPasswordFragment -> {
                    supportActionBar?.hide()
                    navView.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show()
                    navView.visibility = View.VISIBLE
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navgiation_lesson_details) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)

        val currentFragment = findNavController(R.id.nav_host_fragment_activity_main).currentDestination
        if (currentFragment?.id == R.id.navigation_schedule) {
            menu?.findItem(R.id.app_bar_calendar_button)?.isVisible = true // показываем кнопку на нужном фрагменте
        } else {
            menu?.findItem(R.id.app_bar_calendar_button)?.isVisible = false // скрываем кнопку на остальных
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_calendar_button -> {
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
}