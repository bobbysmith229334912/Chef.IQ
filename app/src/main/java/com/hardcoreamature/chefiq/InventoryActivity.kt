package com.hardcoreamature.chefiq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardcoreamature.chefiq.databinding.ActivityInventoryBinding

class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private var ingredients = mutableListOf(
        Ingredient("Tomato", 2),
        Ingredient("Lettuce", 1),
        Ingredient("Cheese", 5)
    )

    override

    fun

            onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter(ingredients, onAmountChanged = { ingredient, newAmount: Int ->
            // Handle the change in amount here
            ingredient.amount = newAmount
            ingredientAdapter.notifyDataSetChanged()
        }, onDelete = { ingredientToDelete ->
            // Handle the deletion of ingredient here
            ingredients.remove(ingredientToDelete)
            ingredientAdapter.notifyDataSetChanged()
        })

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity)
            adapter = ingredientAdapter
        }
    }
}