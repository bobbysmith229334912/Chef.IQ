package com.hardcoreamature.chefiq

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hardcoreamature.chefiq.databinding.ActivityInventoryBinding

class RecipeInputActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private val ingredientsCollection = firestore.collection("ingredients")
    private lateinit var ingredientList: MutableList<Ingredient>
    private lateinit var adapter: IngredientAdapter
    private lateinit var binding: ActivityInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.addIngredientButton.setOnClickListener {
            showAddIngredientDialog()
        }

        fetchIngredients()
    }

    private fun setupRecyclerView() {
        ingredientList = mutableListOf()
        adapter = IngredientAdapter(ingredientList, { ingredient ->
            // handle delete (this is just a placeholder, replace with your own logic)
            ingredientList.remove(ingredient)
            adapter.notifyDataSetChanged()
        }, { ingredient, newAmount ->
            // handle amount change (this is just a placeholder, replace with your own logic)
            val index = ingredientList.indexOf(ingredient)
            if (index != -1) {
                ingredientList[index] = ingredient.copy(amount = newAmount)
                adapter.notifyItemChanged(index)
            }
        })

        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.ingredientsRecyclerView.adapter = adapter
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_ingredient, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val ingredientNameEditText = dialogView.findViewById<EditText>(R.id.ingredientNameEditText)
        val ingredientQuantityEditText = dialogView.findViewById<EditText>(R.id.ingredientQuantityEditText)
        val addButton = dialogView.findViewById<Button>(R.id.addIngredientDialogButton)

        val dialog = builder.create()
        addButton.setOnClickListener {
            val ingredientName = ingredientNameEditText.text.toString().trim()
            val quantityStr = ingredientQuantityEditText.text.toString().trim()
            val quantity = quantityStr.toIntOrNull()
            if (ingredientName.isNotEmpty() && quantity != null) {
                val ingredient = Ingredient(name = ingredientName, amount = quantity)
                addIngredientToFirestore(ingredient)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid name and quantity", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
