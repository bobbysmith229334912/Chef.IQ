package com.hardcoreamature.chefiq

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hardcoreamature.chefiq.databinding.ActivityDashboardBinding

class

DashboardActivity : AppCompatActivity() {

    private

    lateinit

    var binding: ActivityDashboardBinding


    private

    lateinit

    var auth: FirebaseAuth
    private

    lateinit

    var db: FirebaseFirestore
    private lateinit var ingredientAdapter: IngredientAdapter
    private val ingredients = mutableListOf<Ingredient>()

    override

    fun

            onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Set up the RecyclerView
        ingredientAdapter = IngredientAdapter(ingredients,
            { ingredientToDelete -> deleteIngredient(ingredientToDelete.id) },
            { ingredientToUpdate -> updateIngredient(ingredientToUpdate) }
        )

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = ingredientAdapter
        }

        // Fetch ingredients for the current user
        fetchIngredients()

        // Other setup code...
    }

    private fun fetchIngredients() {
        val userId = auth.currentUser?.uid ?: return // Or handle the situation if the user is null

        db.collection("users").document(userId).collection("ingredients")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val fetchedIngredients = snapshot?.documents?.map { document ->
                    Ingredient(
                        id = document.id, // Set the document ID as the ingredient ID
                        name = document.getString("name") ?: "",
                        amount = document.getLong("amount")?.toInt() ?: 0
                    )
                }

                fetchedIngredients?.let {
                    ingredients.clear()
                    ingredients.addAll(it)
                    ingredientAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun updateIngredient(ingredient: Ingredient) {
        val userId = auth.currentUser?.uid ?: return // Or handle this scenario

        db.collection("users").document(userId).collection("ingredients").document(ingredient.id)
            .set(ingredient)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun deleteIngredient(ingredientId: String) {
        val userId = auth.currentUser?.uid ?: return // Or handle this scenario

        db.collection("users").document(userId).collection("ingredients").document(ingredientId)
            .delete()
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}