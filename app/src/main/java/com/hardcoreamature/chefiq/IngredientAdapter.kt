package com.hardcoreamature.chefiq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardcoreamature.chefiq.databinding.ItemIngredientBinding

class

IngredientAdapter(
    private

    val ingredients: MutableList<Ingredient>,
    private

    val onDelete: (String) -> Unit,
    private

    val onAmountChanged: (Ingredient) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override

    fun

            onCreateViewHolder(parent: ViewGroup, viewType:

    Int): IngredientViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override

    fun

            onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override

    fun

            getItemCount(): Int = ingredients.size

    inner

    class IngredientViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient:

                 Ingredient) {
            binding.tvIngredientName.text = ingredient.name
            binding.tvIngredientAmount.text = ingredient.amount.toString()
            binding.btnDelete.setOnClickListener { onDelete(ingredient.id) }
            binding.btnIncrease.setOnClickListener {
                ingredient.amount++
                binding.tvIngredientAmount.text = ingredient.amount.toString()
                onAmountChanged(ingredient)
            }
            binding.btnDecrease.setOnClickListener {
                if (ingredient.amount > 0) {
                    ingredient.amount--
                    binding.tvIngredientAmount.text = ingredient.amount.toString()
                    onAmountChanged(ingredient)
                }
            }
        }
    }
}