package com.ceaver.assin

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ceaver.assin.home.HomeFragmentDirections
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        drawerLayout = main_activity_drawer_layout
        navController = this.findNavController(R.id.main_activity_nav_host_fragment)

        NavigationUI.setupWithNavController(main_activity_navigation_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        main_activity_navigation_view.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (navController.currentDestination!!.id == R.id.homeFragment) {
            // do nothing (do not exit app on back button press).
            // back button should move to 'MARKETS' when in homeFragment, but bug in ViewPager2 component doesn't allow so:
            // https://stackoverflow.com/questions/56311862/viewpager2-default-position
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_actions -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToActionListFragment())
            R.id.nav_alerts -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToAlertListFragment())
            R.id.nav_settings -> Toast.makeText(applicationContext, "No Implementation", Toast.LENGTH_SHORT).show()
            R.id.nav_backup -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToBackupFragment())
            R.id.nav_chat -> Toast.makeText(applicationContext, "No Implementation", Toast.LENGTH_SHORT).show()
            R.id.nav_contact -> Toast.makeText(applicationContext, "No Implementation", Toast.LENGTH_SHORT).show()
            R.id.nav_donate -> Toast.makeText(applicationContext, "No Implementation", Toast.LENGTH_SHORT).show()
            R.id.nav_logging -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToLogListFragment())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}