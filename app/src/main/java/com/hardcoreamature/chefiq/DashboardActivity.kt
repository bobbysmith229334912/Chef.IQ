package com.hardcoreamature.chefiq

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hardcoreamature.chefiq.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setupDrawerNavigation()

        // Set up the listener for the viewIngredientsButton
        binding.viewIngredientsButton.setOnClickListener {
            navigateToIngredientsActivity()
        }
    }

    private fun setupDrawerNavigation() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_ingredients -> navigateToIngredientsActivity()
                R.id.nav_recipe_input -> navigateToRecipeInputActivity()
                // Add more cases for other menu items as needed
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToIngredientsActivity() {
        val intent = Intent(this, IngredientsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecipeInputActivity() {
        val intent = Intent(this, RecipeInputActivity::class.java)
        startActivity(intent)
    }
}
