package com.hardcoreamature.chefiq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hardcoreamature.chefiq.databinding.ActivityRecipeInputBinding

class RecipeInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up UI components and logic for recipe input
        // For example, you can set up a listener for a button to submit a recipe
    }
}
