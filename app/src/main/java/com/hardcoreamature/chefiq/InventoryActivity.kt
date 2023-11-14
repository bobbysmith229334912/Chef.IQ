package com.hardcoreamature.chefiq

import Ingredient
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardcoreamature.chefiq.databinding.ActivityInventoryBinding
import com.hardcoreamature.chefiq.databinding.DialogAddIngredientBinding
import java.util.UUID

class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private val ingredients = mutableListOf<Ingredient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddIngredientButton()
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter(ingredients,
            onDelete = { ingredientId ->
                // Corrected comparison: Comparing String to String
                val index = ingredients.indexOfFirst { it.id.equals(ingredientId) }

                if (index != -1) {
                    ingredients.removeAt(index)
                    ingredientAdapter.notifyItemRemoved(index)
                }
            },
            onAmountChanged = { ingredient ->
                // Corrected comparison: Comparing String to String
                val index = ingredients.indexOfFirst { it.id == ingredient.id }
                if (index != -1) {
                    ingredients[index] = ingredient
                    ingredientAdapter.notifyItemChanged(index)
                }
            }
        )

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity)
            adapter = ingredientAdapter
        }
    }

    private fun setupAddIngredientButton() {
        binding.btnAddIngredient.setOnClickListener {
            val dialogBinding = DialogAddIngredientBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Add Ingredient")
                .setView(dialogBinding.root)
                .setPositiveButton("Add") { _, _ ->
                    val name = dialogBinding.ingredientNameEditText.text.toString()
                    val quantity = dialogBinding.ingredientQuantityEditText.text.toString().toIntOrNull() ?: 0
                    val newIngredient = Ingredient(UUID.randomUUID().toString(), name, quantity)
                    ingredients.add(newIngredient)
                    ingredientAdapter.notifyItemInserted(ingredients.size - 1)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}
