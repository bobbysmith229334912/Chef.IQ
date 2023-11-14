package com.hardcoreamature.chefiq

import Ingredient
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hardcoreamature.chefiq.databinding.ActivityRecipeInputBinding
import com.hardcoreamature.chefiq.databinding.DialogAddIngredientBinding
import java.util.UUID

class IngredientInputActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private val ingredientsCollection = firestore.collection("ingredients")
    private lateinit var ingredientList: MutableList<Ingredient>
    private lateinit var adapter: IngredientAdapter
    private lateinit var binding: ActivityRecipeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddIngredientButton()

        fetchIngredients()
    }

    private fun setupRecyclerView() {
        ingredientList = mutableListOf()
        adapter = IngredientAdapter(ingredientList, { ingredient ->
            // Handle delete
            val index = ingredientList.indexOf(ingredient)
            if (index != -1) {
                ingredientList.removeAt(index)
                adapter.notifyItemRemoved(index)
            }
        }, { ingredient ->
            // Handle amount change (Note: Any amount changes should be reflected in 'ingredient' before this call)
            val index = ingredientList.indexOf(ingredient)
            if (index != -1) {
                ingredientList[index] = ingredient
                adapter.notifyItemChanged(index)
            }
        })

        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.ingredientsRecyclerView.adapter = adapter
    }

    private fun setupAddIngredientButton() {
        binding.addIngredientButton.setOnClickListener {
            showAddIngredientDialog()
        }
    }

    private fun fetchIngredients() {
        ingredientsCollection.get().addOnSuccessListener { snapshot ->
            ingredientList.clear()
            for (document in snapshot) {
                val ingredient = document.toObject(Ingredient::class.java)
                ingredientList.add(ingredient)
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error getting documents: ", e)
        }
    }

    private fun addIngredientToFirestore(ingredient: Ingredient) {
        ingredientsCollection.add(ingredient)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                ingredientList.add(ingredient)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    private fun showAddIngredientDialog() {
        val dialogBinding = DialogAddIngredientBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Ingredient")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.ingredientNameEditText.text.toString().trim()
                val quantity = dialogBinding.ingredientQuantityEditText.text.toString().trim().toIntOrNull() ?: 0
                val newIngredient = Ingredient(UUID.randomUUID().toString(), name, quantity)
                addIngredientToFirestore(newIngredient)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}
