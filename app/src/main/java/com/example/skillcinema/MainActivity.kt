package com.example.skillcinema

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationBar = findViewById<NavigationBarView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.onboardingFragment ||
                destination.id == R.id.galleryImageFullScreenFragment
            ) {
                bottomNavigationBar.visibility = View.GONE
            } else {
                bottomNavigationBar.visibility = View.VISIBLE
            }
        }
    }
}