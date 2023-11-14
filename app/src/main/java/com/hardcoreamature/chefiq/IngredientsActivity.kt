package com.hardcoreamature.chefiq

import Ingredient
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hardcoreamature.chefiq.databinding.ActivityIngredientsBinding
import com.hardcoreamature.chefiq.databinding.DialogAddIngredientBinding
import java.util.UUID

class IngredientsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientsBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private val firestore = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private var ingredientsList: MutableList<Ingredient> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchIngredients()

        // Set up add ingredient button listener
        binding.addIngredientButton.setOnClickListener {
            showAddIngredientDialog()
        }

        // Set up the back button listener
        binding.backButton.setOnClickListener {
            finish() // Close the current activity and return to the previous one
        }
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter(ingredientsList, this::removeIngredient, this::updateIngredient)
        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@IngredientsActivity)
            adapter = ingredientAdapter
        }
    }

    private fun fetchIngredients() {
        user?.uid?.let { userId ->
            firestore.collection("users").document(userId).collection("ingredients")
                .get()
                .addOnSuccessListener { snapshot ->
                    val newIngredients = snapshot.documents.mapNotNull { document ->
                        document.toObject(Ingredient::class.java)?.apply { id = document.id }
                    }
                    updateIngredientsList(newIngredients)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error getting documents: ", e)
                }
        }
    }

    private fun updateIngredientsList(newIngredients: List<Ingredient>) {
        val diffCallback = IngredientDiffCallback(ingredientsList, newIngredients)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        ingredientsList.clear()
        ingredientsList.addAll(newIngredients)
        diffResult.dispatchUpdatesTo(ingredientAdapter)
    }

    private fun removeIngredient(ingredient: Ingredient) {
        user?.uid?.let { userId ->
            firestore.collection("users").document(userId).collection("ingredients").document(ingredient.id)
                .delete()
                .addOnSuccessListener {
                    val index = ingredientsList.indexOf(ingredient)
                    if (index != -1) {
                        ingredientsList.removeAt(index)
                        ingredientAdapter.notifyItemRemoved(index)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error deleting document", e)
                }
        }
    }

    private fun updateIngredient(ingredient: Ingredient) {
        user?.uid?.let { userId ->
            firestore.collection("users").document(userId).collection("ingredients").document(ingredient.id)
                .set(ingredient)
                .addOnSuccessListener {
                    val index = ingredientsList.indexOfFirst { it.id == ingredient.id }
                    if (index != -1) {
                        ingredientsList[index] = ingredient
                        ingredientAdapter.notifyItemChanged(index)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error updating document", e)
                }
        }
    }

    private fun showAddIngredientDialog() {
        val dialogBinding = DialogAddIngredientBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Ingredient")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.ingredientNameEditText.text.toString().trim()
                val quantity = dialogBinding.ingredientQuantityEditText.text.toString().toIntOrNull() ?: 0
                val newIngredient = Ingredient(UUID.randomUUID().toString(), name, quantity)
                addIngredientToFirestore(newIngredient)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun addIngredientToFirestore(ingredient: Ingredient) {
        user?.uid?.let { userId ->
            firestore.collection("users").document(userId).collection("ingredients")
                .add(ingredient)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                    ingredient.id = documentReference.id // Update the ingredient ID
                    ingredientsList.add(ingredient)
                    ingredientAdapter.notifyItemInserted(ingredientsList.size - 1)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }
        }
    }
}

class IngredientDiffCallback(
    private val oldList: List<Ingredient>,
    private val newList: List<Ingredient>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
