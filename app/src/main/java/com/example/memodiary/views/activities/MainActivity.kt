package com.example.memodiary.views.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memodiary.R
import com.example.memodiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //TODO: Back button
        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_memory, R.id.navigation_favourite_memory, R.id.navigation_random_memory
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, mNavController)
        mBinding.navView.setupWithNavController(mNavController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }

    fun hideBottomNavigationView(){
        //it will translate the the navView by its own height in Y direction
        mBinding.navView.animate().translationY(mBinding.navView.height.toFloat()).duration = 300
    }

    fun showBottomNavigationView(){
        //it will translate the the navView by its own height at 0f position
        mBinding.navView.animate().translationY(0f).duration = 300
    }
}