package com.hardcoreamature.chefiq

import Ingredient
import androidx.recyclerview.widget.DiffUtil

class IngredientDiffCallback1(
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
